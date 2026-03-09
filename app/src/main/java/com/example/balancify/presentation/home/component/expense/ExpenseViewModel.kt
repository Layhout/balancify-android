package com.example.balancify.presentation.home.component.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.domain.model.UserModel
import com.example.balancify.domain.use_case.expense.ExpenseUseCases
import com.example.balancify.domain.use_case.user.UserUseCases
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val expenseUseCases: ExpenseUseCases,
    private val userUseCases: UserUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(ExpenseState())
    val state = _state.onStart { loadData() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExpenseState()
    )

    private val _events = Channel<ExpenseEvent>()
    val events = _events.receiveAsFlow()

    private fun alertError(message: String?) {
        _events.trySend(
            ExpenseEvent.OnError(message ?: "Unknown error")
        )
    }

    fun loadData(lastDoc: DocumentSnapshot? = null, isLoading: Boolean = true) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = isLoading) }

            var localUser: UserModel? = null

            if (_state.value.localUser == null) {
                val result = userUseCases.getLocalUser()

                if (result.isSuccess) {
                    localUser = result.getOrNull()
                }
            }

            val result = expenseUseCases.getExpenses(lastDoc)

            if (result.isSuccess) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        expenses = if (lastDoc != null) it.expenses + (result.getOrNull()?.data
                            ?: emptyList()) else (result.getOrNull()?.data ?: emptyList()),
                        canLoadMore = result.getOrNull()?.canLoadMore ?: false,
                        lastDoc = result.getOrNull()?.lastDoc,
                        localUser = localUser,
                    )
                }
            } else {
                alertError(result.exceptionOrNull()?.message)
            }
        }
    }

    fun onAction(action: ExpenseAction) {
        when (action) {
            ExpenseAction.OnLoadMore -> loadData(_state.value.lastDoc)
            ExpenseAction.OnRefresh -> {
                _state.update {
                    it.copy(
                        isRefreshing = true,
                    )
                }
                loadData(isLoading = false)
            }
        }
    }
}