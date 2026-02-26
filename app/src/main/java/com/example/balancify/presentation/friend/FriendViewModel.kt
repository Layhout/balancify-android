package com.example.balancify.presentation.friend

import android.content.ClipData
import androidx.compose.ui.platform.toClipEntry
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.core.constant.FriendStatus
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.use_case.friend.FriendUseCases
import com.example.balancify.domain.use_case.user.UserUseCases
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FriendViewModel(
    private val friendUseCases: FriendUseCases,
    private val userUseCases: UserUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(FriendState())

    val state = _state
        .onStart { loadData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = FriendState()
        )

    private val _events = Channel<FriendEvent>()
    val events = _events.receiveAsFlow()

    private fun alertError(message: String?) {
        _events.trySend(
            FriendEvent.OnError(message ?: "Unknown error")
        )
    }

    suspend fun getInviteLink(): String {
        return viewModelScope.async {
            val result = userUseCases.getLocalUser()
            if (result.isSuccess) {
                return@async "https://balancify.vercel.app/app/invite/${result.getOrNull()?.referralCode}"
            } else {
                alertError(result.exceptionOrNull()?.message)
            }

            return@async ""
        }.await()
    }

    private fun loadData(lastDoc: DocumentSnapshot? = null, isLoading: Boolean = true) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = isLoading) }
            val result = friendUseCases.getFriends(lastDoc)

            if (result.isSuccess) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        friends = if (lastDoc != null) it.friends + (result.getOrNull()?.data
                            ?: emptyList()) else (result.getOrNull()?.data ?: emptyList()),
                        canLoadMore = result.getOrNull()?.canLoadMore ?: false,
                        lastDoc = result.getOrNull()?.lastDoc,
                    )
                }
            } else {
                alertError(result.exceptionOrNull()?.message)
            }
        }
    }

    private fun updateFriendList(id: String, status: FriendStatus) {
        _state.update { friendState ->
            val updatedFriends = friendState.friends.toMutableList()
            val index = updatedFriends.indexOfFirst { it.userId == id }
            updatedFriends[index] =
                updatedFriends[index].copy(status = status)
            friendState.copy(
                enableAllAction = true,
                friends = updatedFriends
            )
        }
    }

    private fun isValidEmail(): Boolean {
        if (_state.value.email.isNotEmpty()) {
            // Email is considered erroneous until it completely matches EMAIL_ADDRESS.
            return android.util.Patterns.EMAIL_ADDRESS.matcher(_state.value.email).matches()
        }

        return false
    }

    fun onAction(action: FriendAction) {
        when (action) {
            is FriendAction.OnLoadMore -> {
                loadData(_state.value.lastDoc)
            }

            is FriendAction.OnRefresh -> {
                _state.update {
                    it.copy(
                        isRefreshing = true,
                    )
                }
                loadData(isLoading = false)
            }

            is FriendAction.OnAddFriendClick -> {
                _state.update { it.copy(addFriendDialogVisible = true) }
            }

            is FriendAction.OnUnfriendClick -> {
                _state.update {
                    it.copy(
                        focusingFriendId = action.id,
                        unfriendConfirmationDialogVisible = true,
                    )
                }
            }

            is FriendAction.OnConfirmUnfriendClick -> {
                viewModelScope.launch {
                    _state.update { it.copy(enableAllAction = false) }
                    val result = friendUseCases.unfriend(_state.value.focusingFriendId!!)
                    if (result.isSuccess) {
                        _state.update { friendState ->
                            friendState.copy(
                                focusingFriendId = null,
                                unfriendConfirmationDialogVisible = false,
                                enableAllAction = true,
                                friends = friendState.friends.filter {
                                    it.userId != _state.value.focusingFriendId
                                }
                            )
                        }
                    } else {
                        alertError(result.exceptionOrNull()?.message)
                    }
                }
            }

            is FriendAction.OnDismissUnfriendClick -> {
                _state.update {
                    it.copy(
                        focusingFriendId = null,
                        unfriendConfirmationDialogVisible = false,
                    )
                }
            }

            is FriendAction.OnAcceptFriendClick -> {
                viewModelScope.launch {
                    _state.update { it.copy(enableAllAction = false) }
                    val result = friendUseCases.acceptFriend(action.id)
                    if (result.isSuccess) {
                        updateFriendList(action.id, FriendStatus.ACCEPTED)
                    } else {
                        alertError(result.exceptionOrNull()?.message)
                    }
                }
            }

            is FriendAction.OnRejectFriendClick -> {
                viewModelScope.launch {
                    _state.update { it.copy(enableAllAction = false) }
                    val result = friendUseCases.rejectFriend(action.id)
                    if (result.isSuccess) {
                        updateFriendList(action.id, FriendStatus.REJECTED)
                    } else {
                        alertError(result.exceptionOrNull()?.message)
                    }
                }
            }

            is FriendAction.OnDismissAddFriendDialog -> {
                _state.update {
                    it.copy(
                        addFriendDialogVisible = false,
                        email = "",
                    )
                }
            }

            is FriendAction.OnEmailUpdate -> {
                _state.update { it.copy(email = action.email, invalidEmail = false) }
            }

            is FriendAction.OnAddFriendConfirmClick -> {
                if (!isValidEmail()) {
                    _state.update { it.copy(invalidEmail = true) }
                } else {
                    viewModelScope.launch {
                        val localUserResult = userUseCases.getLocalUser()
                        if (
                            localUserResult.isSuccess &&
                            localUserResult.getOrNull()?.email == _state.value.email
                        ) {
                            alertError("You cannot add yourself as a friend.")
                            return@launch
                        }
                        if (localUserResult.isFailure) {
                            alertError(localUserResult.exceptionOrNull()?.message)
                            return@launch
                        }

                        _state.update { it.copy(enableAllAction = false) }
                        val result = friendUseCases.addFriendByEmail(_state.value.email)
                        if (result.isSuccess) {
                            _state.update {
                                it.copy(
                                    enableAllAction = true,
                                    addFriendDialogVisible = false,
                                    email = "",
                                    friends = listOf(
                                        result.getOrNull() ?: FriendModel()
                                    ) + it.friends
                                )
                            }
                        } else if (result.isFailure && result.exceptionOrNull()?.message == "USER404") {
                            val inviteLink = getInviteLink()

                            _state.update {
                                it.copy(
                                    enableAllAction = true,
                                    addFriendDialogVisible = false,
                                    email = "",
                                    inviteLinkBottomSheetVisible = true,
                                    inviteLink = inviteLink,
                                )
                            }
                        } else {
                            alertError(result.exceptionOrNull()?.message)
                        }
                    }
                }
            }

            is FriendAction.OnDismissInviteLinkBottomSheet -> {
                _state.update { it.copy(inviteLinkBottomSheetVisible = false) }
            }

            is FriendAction.OnInviteLinkClick -> {
                viewModelScope.launch {
                    val clipData =
                        ClipData.newPlainText(
                            "Invite Link",
                            state.value.inviteLink
                        )
                    action.clipboard.setClipEntry(clipData.toClipEntry())
                    _state.update { it.copy(isInviteLinkCopied = true) }
                    delay(3000)
                    _state.update { it.copy(isInviteLinkCopied = false) }
                }
            }

            is FriendAction.OnShareLinkClick -> {
                viewModelScope.launch {
                    _events.send(FriendEvent.OnShareLinkClicked)
                }
            }
        }
    }
}