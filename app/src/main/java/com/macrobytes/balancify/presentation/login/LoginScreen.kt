package com.macrobytes.balancify.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.macrobytes.balancify.core.util.ObserveAsEvents
import com.macrobytes.balancify.presentation.login.component.BackgroundGradient
import com.macrobytes.balancify.presentation.login.component.LoginAppLogo
import com.macrobytes.balancify.presentation.login.component.LoginFooter
import com.macrobytes.balancify.presentation.login.component.LoginFormBottomSheet
import com.macrobytes.balancify.presentation.login.component.ResetPasswordDialog
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginComplete: () -> Unit,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(null) {
        viewModel.onAction(LoginAction.OnScreenLoad(context, onLoginComplete))
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is LoginEvent.OnError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    BackgroundGradient {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            LoginAppLogo()
            LoginFooter(onLoginComplete = onLoginComplete)
        }
    }

    if (state.value.showLoginFormBottomSheet)
        LoginFormBottomSheet(
            onDismiss = {
                viewModel.onAction(
                    LoginAction.OnLoginFormBottomSheetToggle
                )
            },
            onLoginComplete = onLoginComplete,
        )
    if (state.value.showResetPasswordDialog)
        ResetPasswordDialog(
            onDismiss = {
                viewModel.onAction(
                    LoginAction.OnResetPasswordEmailChange("")
                )
                viewModel.onAction(
                    LoginAction.OnResetPasswordDialogToggle
                )
            }
        )
}
