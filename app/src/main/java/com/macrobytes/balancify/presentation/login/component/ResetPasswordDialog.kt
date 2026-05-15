package com.macrobytes.balancify.presentation.login.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.macrobytes.balancify.core.constant.BORDER_RADIUS_MD
import com.macrobytes.balancify.presentation.login.LoginAction
import com.macrobytes.balancify.presentation.login.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordDialog(
    viewModel: LoginViewModel = koinViewModel(),
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(BORDER_RADIUS_MD),
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    "Reset Password",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "Enter your email address to receive a password reset link.",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    label = { Text("Email") },
                    value = state.value.resetPasswordEmail,
                    onValueChange = {
                        viewModel.onAction(LoginAction.OnResetPasswordEmailChange(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.value.isResetPasswordEmailInvalid,
                    shape = RoundedCornerShape(BORDER_RADIUS_MD),
                    supportingText = {
                        if (state.value.isResetPasswordEmailInvalid) {
                            Text("Email is invalid.")
                        }
                    }
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextButton(
                        onClick = onDismiss,
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = {
                            viewModel.onAction(LoginAction.OnResetPasswordClick(context))
                        },
                    ) { Text("Send") }
                }
            }
        }
    }
}