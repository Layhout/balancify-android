package com.example.balancify.presentation.expense_form.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.presentation.expense_form.ExpenseFormViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpenseFormFooter(
    viewModel: ExpenseFormViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Button(
        onClick = {

        },
        modifier = Modifier.fillMaxWidth(),
        enabled = !state.value.isLoading,
    ) {
        if (state.value.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(if (state.value.isEditing) "Update" else "Create")
    }
}