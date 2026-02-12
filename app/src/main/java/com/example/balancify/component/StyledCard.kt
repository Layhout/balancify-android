package com.example.balancify.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.balancify.core.constant.BORDER_RADIUS_MD
import com.example.balancify.core.constant.BORDER_RADIUS_SM

enum class CardOrder {
    FIRST, LAST, MIDDLE, ALONE
}

@Composable
fun StyledCard(
    modifier: Modifier = Modifier,
    order: CardOrder = CardOrder.ALONE,
    colors: CardColors = CardDefaults.cardColors(),
    content: @Composable ColumnScope.() -> Unit,
) {

    val shape: RoundedCornerShape = when (order) {
        CardOrder.FIRST -> RoundedCornerShape(
            topStart = BORDER_RADIUS_MD,
            topEnd = BORDER_RADIUS_MD,
            bottomStart = BORDER_RADIUS_SM,
            bottomEnd = BORDER_RADIUS_SM,
        )


        CardOrder.LAST -> RoundedCornerShape(
            topStart = BORDER_RADIUS_SM,
            topEnd = BORDER_RADIUS_SM,
            bottomStart = BORDER_RADIUS_MD,
            bottomEnd = BORDER_RADIUS_MD,
        )


        CardOrder.MIDDLE -> RoundedCornerShape(BORDER_RADIUS_SM)
        CardOrder.ALONE -> RoundedCornerShape(BORDER_RADIUS_MD)
    }

    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
        content = content,
    )
}