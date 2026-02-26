package com.example.balancify.presentation.friend.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.CardOrder
import com.example.balancify.component.PillBox
import com.example.balancify.component.UserListCard
import com.example.balancify.core.constant.FriendStatus
import com.example.balancify.core.ext.darken
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.model.UserModel
import com.example.balancify.presentation.friend.FriendAction
import com.example.balancify.presentation.friend.FriendViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendCard(
    data: FriendModel = FriendModel(),
    viewModel: FriendViewModel = koinViewModel(),
    order: CardOrder = CardOrder.ALONE,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    UserListCard(
        order = order,
        user = data.user ?: UserModel(),
        action = {
            Row {
                when (data.status) {
                    FriendStatus.ACCEPTED -> IconButton(
                        enabled = state.value.enableAllAction,
                        modifier = Modifier.size(38.dp),
                        onClick = {
                            viewModel.onAction(
                                FriendAction.OnUnfriendClick(id = data.userId)
                            )
                        },
                    ) {
                        Icon(
                            Icons.Outlined.PersonRemove,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = null
                        )
                    }

                    FriendStatus.REQUESTING -> {
                        IconButton(
                            enabled = state.value.enableAllAction,
                            modifier = Modifier.size(38.dp),
                            onClick = {
                                viewModel.onAction(
                                    FriendAction.OnRejectFriendClick(id = data.userId)
                                )
                            },
                        ) {
                            Icon(
                                Icons.Outlined.Close,
                                tint = MaterialTheme.colorScheme.error,
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            enabled = state.value.enableAllAction,
                            modifier = Modifier.size(38.dp),
                            onClick = {
                                viewModel.onAction(
                                    FriendAction.OnAcceptFriendClick(id = data.userId)
                                )
                            },
                        ) {
                            Icon(
                                Icons.Outlined.Check,
                                tint = Color.Green.darken(0.6f),
                                contentDescription = null
                            )
                        }
                    }

                    FriendStatus.REJECTED -> PillBox(
                        backgroundColor = MaterialTheme.colorScheme.errorContainer,
                    ) {
                        Text("Rejected", style = MaterialTheme.typography.labelSmall)
                    }


                    FriendStatus.PENDING -> PillBox(
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Text("Pending", style = MaterialTheme.typography.labelSmall)
                    }

                    FriendStatus.UNFRIEND -> PillBox {
                        Text("Unfriend", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    )
}