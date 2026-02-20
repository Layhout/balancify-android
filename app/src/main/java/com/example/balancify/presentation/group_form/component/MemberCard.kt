package com.example.balancify.presentation.group_form.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.balancify.component.Avatar
import com.example.balancify.component.CardOrder
import com.example.balancify.component.StyledCard
import com.example.balancify.domain.model.FriendModel
import com.example.balancify.presentation.group_form.GroupFormAction
import com.example.balancify.presentation.group_form.GroupFormViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MemberCard(
    viewModel: GroupFormViewModel = koinViewModel(),
    item: FriendModel,
    order: CardOrder
) {
    StyledCard(
        order = order,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(
                imageUrl = item.user?.imageUrl ?: "",
                modifier = Modifier.size(42.dp)
            )
            Text(
                item.name,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.weight(1f)
            )
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
    }
}