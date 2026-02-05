package com.example.balancify.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Empty(
    emptyText: String = "No Data."
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(34.dp))
        Text(
            "(-_-;)",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Black
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(emptyText)
    }
}