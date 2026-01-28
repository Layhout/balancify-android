package com.example.balancify.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.balancify.core.getInitials

@Composable
fun Avatar(
    imageUrl: String,
    modifier: Modifier = Modifier,
    fallbackText: String = "",
    bgColor: Color = Color.Gray,
    textStyle: TextStyle = TextStyle(fontSize = 16.sp)
) {
    var imageAlpha by rememberSaveable { mutableFloatStateOf(0f) }
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
        ) {
            Text(text = fallbackText.getInitials(), style = textStyle)
        }
        if (imageUrl.isNotEmpty()) {
            AsyncImage(
                model = imageUrl,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(imageAlpha),
                contentScale = ContentScale.Crop,
                contentDescription = fallbackText,
                onSuccess = {
                    imageAlpha = 1f
                }
            )
        }
    }
}