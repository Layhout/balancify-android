package com.example.balancify.presentation.friend.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.balancify.component.Avatar
import com.example.balancify.component.PillBox
import com.example.balancify.core.constant.BORDER_RADIUS_MD
import com.example.balancify.core.constant.FriendStatus
import com.example.balancify.core.ext.darken
import com.example.balancify.domain.model.FriendModel

@Composable
fun FriendCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(BORDER_RADIUS_MD),
    data: FriendModel = FriendModel()
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(
                imageUrl = data.user?.imageUrl ?: "",
                modifier = Modifier.size(42.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    data.user?.name ?: "",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(data.user?.email ?: "", style = MaterialTheme.typography.labelMedium)
            }
            Row {
                when (data.status) {
                    FriendStatus.ACCEPTED -> IconButton(
                        modifier = Modifier.size(38.dp),
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(
                            Icons.Outlined.PersonRemove,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = null
                        )
                    }

                    FriendStatus.REQUESTING -> {
                        IconButton(
                            modifier = Modifier.size(38.dp),
                            onClick = { /*TODO*/ },
                        ) {
                            Icon(
                                Icons.Outlined.Close,
                                tint = MaterialTheme.colorScheme.error,
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            modifier = Modifier.size(38.dp),
                            onClick = { /*TODO*/ },
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
    }
}