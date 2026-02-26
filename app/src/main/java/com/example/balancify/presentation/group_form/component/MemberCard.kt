package com.example.balancify.presentation.group_form.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.balancify.component.CardOrder
import com.example.balancify.component.UserListCard
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.domain.model.UserModel
import com.example.balancify.presentation.group_form.GroupFormAction
import com.example.balancify.presentation.group_form.GroupFormViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MemberCard(
    viewModel: GroupFormViewModel = koinViewModel(),
    item: FriendModel,
    order: CardOrder
) {
    UserListCard(
        order = order,
        user = item.user ?: UserModel(),
        action = {
            IconButton(
                modifier = Modifier.size(38.dp),
                onClick = {
                    viewModel.onAction(
                        GroupFormAction.OnRemoveMemberClick(
                            item.userId
                        )
                    )
                }
            ) {
                Icon(
                    Icons.Outlined.PersonRemove,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null
                )
            }
        }
    )
}