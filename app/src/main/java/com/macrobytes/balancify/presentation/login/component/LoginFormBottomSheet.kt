package com.macrobytes.balancify.presentation.login.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.macrobytes.balancify.R
import com.macrobytes.balancify.component.TextDivider
import com.macrobytes.balancify.core.constant.BORDER_RADIUS_MD
import com.macrobytes.balancify.presentation.login.LoginAction
import com.macrobytes.balancify.presentation.login.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginFormBottomSheet(
    viewModel: LoginViewModel = koinViewModel(),
    onDismiss: () -> Unit,
    onLoginComplete: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val textFieldState = remember { TextFieldState() }
    var showPassword by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val state = viewModel.state.collectAsStateWithLifecycle()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            OutlinedTextField(
                enabled = state.value.isEnableAllAction,
                value = state.value.email,
                singleLine = true,
                onValueChange = { value ->
                    viewModel.onAction(LoginAction.OnEmailChange(value))
                },
                label = { Text("Email") },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(BORDER_RADIUS_MD),
                isError = state.value.isEmailInvalid,
                supportingText = {
                    if (state.value.isEmailInvalid)
                        Text("Email is invalid.")
                }
            )
            OutlinedSecureTextField(
                enabled = state.value.isEnableAllAction,
                label = { Text("Password") },
                state = textFieldState,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(BORDER_RADIUS_MD),
                isError = state.value.isPasswordInvalid,
                supportingText = {
                    if (state.value.isPasswordInvalid)
                        Text("Please choose a stronger password.")
                },
                textObfuscationMode =
                    if (showPassword) {
                        TextObfuscationMode.Visible
                    } else {
                        TextObfuscationMode.RevealLastTyped
                    },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = null
                        )
                    }
                }
            )
            if (!state.value.isSignUp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        onClick = {
                            viewModel.onAction(LoginAction.OnResetPasswordDialogToggle)
                        }
                    ) {
                        Text("Forgot password")
                    }
                }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.onAction(LoginAction.OnPasswordChange(textFieldState.text.toString()))
                    viewModel.onAction(LoginAction.OnSubmitLoginForm)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.value.isEnableAllAction || !state.value.isLoading,
            ) {
                Text(if (state.value.isSignUp) "Sign Up" else "Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextDivider("or")
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    viewModel.onAction(LoginAction.OnSignInClick(context, onLoginComplete))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = state.value.isEnableAllAction || !state.value.isLoading,
            ) {
                Image(
                    ImageVector.vectorResource(R.drawable.google_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.width(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue with Google")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}