package com.example.balancify.presentation.home.component.account.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.outlined.PeopleAlt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.balancify.component.CardOrder
import com.example.balancify.presentation.home.component.account.AccountAction
import com.example.balancify.presentation.home.component.account.AccountViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun OptionCard(
    viewModel: AccountViewModel = koinViewModel()
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CardItem(
            icon = Icons.Outlined.PeopleAlt,
            label = "Friends",
            order = CardOrder.FIRST
        ) {
            viewModel.onAction(AccountAction.OnFriendClick)
        }
        Spacer(modifier = Modifier.height(2.dp))
        CardItem(
            icon = Icons.Outlined.Settings,
            label = "Settings",
            order = CardOrder.MIDDLE
        )
        Spacer(modifier = Modifier.height(2.dp))
        CardItem(
            onClick = { viewModel.onAction(AccountAction.OnDevBlogClick) },
            icon = Icons.AutoMirrored.Outlined.Article,
            label = "Dev Blogs",
            order = CardOrder.LAST

        )
    }
}