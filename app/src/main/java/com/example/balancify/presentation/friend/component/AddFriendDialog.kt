package com.example.balancify.presentation.friend.component

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.core.constant.BORDER_RADIUS_MD
import com.example.balancify.presentation.friend.FriendAction
import com.example.balancify.presentation.friend.FriendViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddFriendDialog(
    viewModel: FriendViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Dialog(
        onDismissRequest = {
            viewModel.onAction(FriendAction.OnDismissAddFriendDialog)
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(BORDER_RADIUS_MD),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    "Add a Friend",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "Enter your friend’s email to add them.",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    label = { Text("Email") },
                    value = state.value.email,
                    onValueChange = { viewModel.onAction(FriendAction.OnEmailUpdate(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.value.invalidEmail,
                    supportingText = {
                        if (state.value.invalidEmail) {
                            Text("Invalid Email.")
                        }
                    }
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextButton(
                        onClick = {
                            viewModel.onAction(FriendAction.OnDismissAddFriendDialog)
                        }
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = {
                            viewModel.onAction(FriendAction.OnAddFriendConfirmClick)
                        },
                        enabled = state.value.enableAllAction && state.value.email.isNotEmpty()
                    ) { Text("Add") }
                }
            }
        }
    }
}
