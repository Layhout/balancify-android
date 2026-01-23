package com.example.balancify.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.domain.repository.UserRepository
import com.example.balancify.service.AuthResult
import com.example.balancify.service.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authService: AuthService,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    fun loadSignInBottomSheet(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            processSignInResult(authService.signInWithBottomSheet(context), onSuccess)
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onClickSignIn(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            processSignInResult(authService.signInWithDialog(context), onSuccess)
            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun processSignInResult(result: AuthResult, onSuccess: () -> Unit) {
        if (result.isNewUser && result.user != null) {
            userRepository.addUser(result.user)
        }
        if (result.successful) {
            onSuccess()
        }
    }
}
