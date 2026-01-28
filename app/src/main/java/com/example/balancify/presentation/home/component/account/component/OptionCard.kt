package com.example.balancify.presentation.home.component.account.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.outlined.PeopleAlt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OptionCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CardItem(
                icon = Icons.Outlined.PeopleAlt,
                label = "Friends"
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.background,
                thickness = 2.dp
            )

            CardItem(
                icon = Icons.Outlined.Settings,
                label = "Settings"
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.background,
                thickness = 2.dp
            )

            CardItem(
                icon = Icons.AutoMirrored.Outlined.Article,
                label = "Dev Blogs"
            )
        }
    }
}