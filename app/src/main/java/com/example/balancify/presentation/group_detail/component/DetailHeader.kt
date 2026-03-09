package com.example.balancify.presentation.group_detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.Empty
import com.example.balancify.presentation.group_detail.GroupDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailHeader(
    viewModel: GroupDetailViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxWidth()) {
        Text("Name", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            state.value.group.name,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(Modifier.height(16.dp))
        Text("Description", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            state.value.group.description.ifBlank { "--" },
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(Modifier.height(16.dp))
        Text("Create By", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            if (state.value.isCreateByLocalUser) "You" else
                state.value.group.getOwner().ifBlank { "--" },
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(Modifier.height(16.dp))
        Text("Expenses", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(4.dp))
        if (!state.value.isLoading && state.value.expenses.isEmpty()) {
            Empty()
        }
    }
}