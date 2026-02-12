package com.example.balancify.presentation.home.component.account.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.balancify.component.CardOrder
import com.example.balancify.presentation.home.component.account.AccountAction
import com.example.balancify.presentation.home.component.account.AccountViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LogoutCard(viewModel: AccountViewModel = koinViewModel()) {
    CardItem(
        icon = Icons.AutoMirrored.Rounded.Logout, label = "Logout",
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
        order = CardOrder.ALONE,
        onClick = {
            viewModel.onAction(AccountAction.OnLogoutClick)
        },
    )
}