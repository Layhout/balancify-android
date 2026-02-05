package com.example.balancify.presentation.friend

import com.example.balancify.domain.model.FriendModel
import com.google.firebase.firestore.DocumentSnapshot

data class FriendState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = false,
    val enableAllAction: Boolean = true,
    val unfriendConfirmationDialogVisible: Boolean = false,
    val addFriendDialogVisible: Boolean = false,
    val invalidEmail: Boolean = false,
    val inviteLinkBottomSheetVisible: Boolean = false,
    val isInviteLinkCopied: Boolean = false,
    val email: String = "",
    val inviteLink: String = "",
    val friends: List<FriendModel> = emptyList(),
    val lastDoc: DocumentSnapshot? = null,
    val focusingFriendId: String? = null
)
