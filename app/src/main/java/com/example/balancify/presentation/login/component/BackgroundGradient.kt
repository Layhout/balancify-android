package com.example.balancify.presentation.login.component

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BackgroundGradient(content: @Composable () -> Unit) {
    val density = LocalDensity.current

    val screenWidthPx = with(density) {
        currentWindowDpSize().width.roundToPx()
    }
    val smallDimension = minOf(
        currentWindowDpSize().width,
        currentWindowDpSize().height
    )
    val smallDimensionPx = with(density) {
        smallDimension.roundToPx()
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val isAtLeastAndroid12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isAtLeastAndroid12) {
                        Modifier.blur(smallDimension / 3f)
                    } else Modifier
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (isAtLeastAndroid12) primaryColor else primaryColor.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.background
                        ),
                        center = Offset(
                            x = screenWidthPx / 2f,
                            y = -100f
                        ),
                        radius = smallDimensionPx / 2f
                    )
                )
        )
        content()
    }
}