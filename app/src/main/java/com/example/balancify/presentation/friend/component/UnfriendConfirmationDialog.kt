package com.example.balancify.presentation.friend.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.balancify.presentation.friend.FriendAction
import com.example.balancify.presentation.friend.FriendViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnfriendConfirmationDialog(
    viewModel: FriendViewModel = koinViewModel(),
) {
    AlertDialog(
        title = {
            Text("Are you sure you want to unfriend?")
        },
        text = {
            Text("This action cannot be undone.")
        },
        onDismissRequest = {
            viewModel.onAction(FriendAction.OnDismissUnfriendClick)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.onAction(FriendAction.OnConfirmUnfriendClick)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.onAction(FriendAction.OnDismissUnfriendClick)
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}