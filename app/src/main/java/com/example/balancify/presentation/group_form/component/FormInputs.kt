package com.example.balancify.presentation.group_form.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.core.constant.BORDER_RADIUS_MD
import com.example.balancify.presentation.group_form.GroupFormAction
import com.example.balancify.presentation.group_form.GroupFormViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FormInputs(
    viewModel: GroupFormViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = state.value.name,
            onValueChange = {
                viewModel.onAction(GroupFormAction.OnNameChange(it))
            },
            label = { Text("Name *") },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(BORDER_RADIUS_MD),
            isError = state.value.isNameInvalid,
            supportingText = {
                if (state.value.isNameInvalid)
                    Text("Name cannot be empty")
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = state.value.description,
            onValueChange = {
                viewModel.onAction(GroupFormAction.OnDescriptionChange(it))
            },
            label = { Text("Description") },
            shape = RoundedCornerShape(BORDER_RADIUS_MD),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}