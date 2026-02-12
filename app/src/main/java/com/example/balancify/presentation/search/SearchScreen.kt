package com.example.balancify.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.balancify.component.AppBar
import com.example.balancify.core.constant.BORDER_RADIUS_MD
import com.example.balancify.core.constant.SearchType

@Composable
fun SearchScreen(
    type: SearchType,
    onBackClick: () -> Unit,
) {

    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Scaffold(
            topBar = {
                AppBar(
                    "Search ${if (type == SearchType.FRIEND) "Friend" else "Group"}",
                    onBackClick
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 16.dp),
            ) {
                OutlinedTextField(
                    label = { Text("Search...") },
                    value = "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(BORDER_RADIUS_MD),
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {}
            }
        }
    }
}