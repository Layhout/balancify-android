package com.example.balancify.presentation.home.component.account.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.Avatar
import com.example.balancify.presentation.home.component.account.AccountViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserProFile(viewModel: AccountViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val user = state.value.user

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        Avatar(
            imageUrl = user?.imageUrl ?: "",
            modifier = Modifier
                .size(110.dp),
            fallbackText = user?.name ?: "",
            bgColor = Color(user?.profileBgColor?.toColorInt() ?: 0),
            textStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = user?.name ?: "N/A",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = user?.email ?: "", textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodySmall,
    )
}