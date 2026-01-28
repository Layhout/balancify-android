package com.example.balancify.presentation.home.component.account.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.balancify.presentation.home.component.account.AccountAction
import com.example.balancify.presentation.home.component.account.AccountViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LogoutCard(viewModel: AccountViewModel = koinViewModel()) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
    ) {
        CardItem(
            icon = Icons.AutoMirrored.Rounded.Logout, label = "Logout",
            onClick = {
                viewModel.onAction(AccountAction.OnLogoutClick)
            },
        )
    }
}