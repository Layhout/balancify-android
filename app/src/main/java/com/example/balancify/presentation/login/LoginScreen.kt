package com.example.balancify.presentation.login

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
import com.example.balancify.presentation.login.component.AppLogo
import com.example.balancify.presentation.login.component.BackgroundGradient
import com.example.balancify.presentation.login.component.Footer
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginComplete: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(null) {
        viewModel.onAction(LoginAction.OnScreenLoad(context, onLoginComplete))
    }

    BackgroundGradient {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            AppLogo()
            Footer(onLoginComplete = onLoginComplete)
        }
    }
}
