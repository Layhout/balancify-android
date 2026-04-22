package com.example.balancify.presentation.group_detail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.AppBar
import com.example.balancify.presentation.group_detail.GroupDetailAction
import com.example.balancify.presentation.group_detail.GroupDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GroupDetailAppBar(
    viewModel: GroupDetailViewModel = koinViewModel(),
    onNavigateToGroupFrom: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    AppBar("Group Detail", onBackClick) {
        IconButton(
            enabled = state.value.enableAllAction,
            onClick = {
                viewModel.onAction(GroupDetailAction.OnDropdownMenuToggle)
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More options",
            )
        }
        DropdownMenu(
            expanded = state.value.showDropdown,
            onDismissRequest = {
                viewModel.onAction(GroupDetailAction.OnDropdownMenuToggle)
            },
        ) {
            if (state.value.isCreateByLocalUser)
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(12.dp))
                            Text("Edit")
                        }
                    },
                    enabled = state.value.enableAllAction,
                    onClick = {
                        viewModel.onAction(GroupDetailAction.OnDropdownMenuToggle)
                        onNavigateToGroupFrom(state.value.group.id)
                    }
                )
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("Leave")
                    }
                },
                enabled = state.value.enableAllAction,
                onClick = {
                    viewModel.onAction(GroupDetailAction.OnLeaveGroupClick)
                }
            )
        }
    }
}