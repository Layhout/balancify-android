package com.macrobytes.balancify.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.macrobytes.balancify.core.ext.format
import com.macrobytes.balancify.domain.model.GroupModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupCard(
    item: GroupModel,
    order: CardOrder,
    onClick: () -> Unit
) {
    StyledCard(
        modifier = Modifier
            .fillMaxWidth(),
        order = order,
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    onClick = onClick
                )
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val createdDate: String? = item.createdAt?.format("dd MMM yyyy")

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(createdDate ?: "--", style = MaterialTheme.typography.labelMedium)
            }
            Box {
                StackAvatar {
                    item.members.take(3).forEach { member ->
                        Avatar(
                            imageUrl = member.imageUrl,
                            modifier = Modifier.size(32.dp),
                            fallbackText = member.name,
                            bgColor = Color(member.profileBgColor.toColorInt()),
                        )
                    }
                    if (item.members.size > 3)
                        Avatar(
                            imageUrl = "",
                            modifier = Modifier.size(32.dp),
                            fallbackText = "+ ${item.members.size - 3}",
                            bgColor = MaterialTheme.colorScheme.outlineVariant,
                        )
                }
            }
            Icon(
                Icons.Outlined.ChevronRight,
                contentDescription = null
            )
        }
    }
}