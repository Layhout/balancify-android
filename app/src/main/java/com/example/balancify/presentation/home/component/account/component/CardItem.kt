package com.example.balancify.presentation.home.component.account.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.balancify.component.CardOrder
import com.example.balancify.component.StyledCard

@Composable
fun CardItem(
    icon: ImageVector,
    label: String,
    colors: CardColors = CardDefaults.cardColors(),
    order: CardOrder,
    onClick: () -> Unit = {},
) {
    StyledCard(
        modifier = Modifier.fillMaxWidth(),
        colors = colors,
        order = order,
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    onClick = onClick,
                )
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon,
                null,
                modifier = Modifier.size(24.dp),
            )
            Text(
                label,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Icon(
                Icons.Rounded.ChevronRight,
                null
            )
        }
    }
}