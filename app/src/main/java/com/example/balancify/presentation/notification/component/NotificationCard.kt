package com.example.balancify.presentation.notification.component

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.CardOrder
import com.example.balancify.component.StyledCard
import com.example.balancify.domain.model.NotificationModel
import com.example.balancify.domain.model.NotificationType.EXPENSE
import com.example.balancify.domain.model.NotificationType.FRIEND_REQUEST
import com.example.balancify.domain.model.NotificationType.GROUP
import com.example.balancify.presentation.notification.NotificationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NotificationCard(
    data: NotificationModel,
    viewModel: NotificationViewModel = koinViewModel(),
    order: CardOrder = CardOrder.ALONE,
    onClick: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val timeFromNow = if (data.createdAt != null) DateUtils.getRelativeTimeSpanString(
        data.createdAt.time,
        System.currentTimeMillis(),
        DateUtils.DAY_IN_MILLIS
    ) else ""


    StyledCard(
        modifier = Modifier
            .fillMaxWidth(),
        order
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            BadgedBox(
                badge = {
                    if (data.isUnread(state.value.localUser?.id ?: ""))
                        Badge()
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    val iconSize = 18.dp
                    when (data.type) {
                        FRIEND_REQUEST -> Icon(
                            Icons.Outlined.PersonAdd,
                            contentDescription = null,
                            modifier = Modifier.size(iconSize)
                        )

                        GROUP -> Icon(
                            Icons.Outlined.GroupAdd,
                            contentDescription = null,
                            modifier = Modifier.size(iconSize)
                        )

                        EXPENSE -> Icon(
                            Icons.Outlined.AttachMoney,
                            contentDescription = null,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    data.title, style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(data.description, style = MaterialTheme.typography.bodySmall)
            }
            Text(timeFromNow.toString(), style = MaterialTheme.typography.labelSmall)
        }
    }

}