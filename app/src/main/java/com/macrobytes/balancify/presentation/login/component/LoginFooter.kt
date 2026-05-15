package com.macrobytes.balancify.presentation.login.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.macrobytes.balancify.presentation.login.LoginAction
import com.macrobytes.balancify.presentation.login.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginFooter(viewModel: LoginViewModel = koinViewModel(), onLoginComplete: () -> Unit) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()
    val isLoading = state.value.isLoading

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "Welcome to Balancify",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "A clean, no-fuss way to manage shared expenses with anyone, anywhere. Because fairness shouldn’t be complicated.",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
//                viewModel.onAction(LoginAction.OnSignInClick(context, onLoginComplete))
                viewModel.onAction(LoginAction.OnIsSignUp(false))
                viewModel.onAction(LoginAction.OnLoginFormBottomSheetToggle)
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (state.value.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = {
//                viewModel.onAction(LoginAction.OnSignInClick(context, onLoginComplete))
                viewModel.onAction(LoginAction.OnIsSignUp(true))
                viewModel.onAction(LoginAction.OnLoginFormBottomSheetToggle)
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (state.value.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text("Sign Up")
        }
    }
}