package com.example.balancify.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.balancify.domain.model.UserModel

@Composable
fun UserListCard(
    modifier: Modifier = Modifier,
    order: CardOrder = CardOrder.ALONE,
    colors: CardColors = CardDefaults.cardColors(),
    user: UserModel,
    subTitleContent: (@Composable () -> Unit)? = null,
    hideAvatar: Boolean = false,
    action: (@Composable () -> Unit)? = null,
) {
    StyledCard(modifier.fillMaxWidth(), order, colors) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (!hideAvatar)
                Avatar(
                    imageUrl = user.imageUrl,
                    modifier = Modifier.size(42.dp),
                    fallbackText = user.name,
                )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    user.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
//                Spacer(modifier = Modifier.height(2.dp))
                if (subTitleContent != null)
                    subTitleContent.invoke()
                else
                    Text(
                        user.email,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium
                    )
            }
            action?.invoke()
        }
    }
}