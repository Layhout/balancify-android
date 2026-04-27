package com.example.balancify.presentation.expense_form.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.core.constant.BORDER_RADIUS_MD
import com.example.balancify.core.constant.BORDER_RADIUS_SM
import com.example.balancify.presentation.expense_form.ExpenseFormAction
import com.example.balancify.presentation.expense_form.ExpenseFormViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpenseNameInput(
    viewModel: ExpenseFormViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.padding(bottom = 4.dp)) {
            Box(
                modifier = Modifier
                    .clickable(
                        enabled = state.value.isEnableAllAction,
                        onClick = {
                            viewModel.onAction(
                                ExpenseFormAction.OnIconFormBottomSheetToggle
                            )
                        }
                    )
                    .size(56.dp)
                    .background(
                        color = Color(state.value.iconBgColor.toColorInt()),
                        shape = RoundedCornerShape(
                            size = BORDER_RADIUS_SM,
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    ImageVector.vectorResource(
                        state.value.icon.id
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
        OutlinedTextField(
            enabled = state.value.isEnableAllAction,
            value = state.value.name,
            singleLine = true,
            onValueChange = { value ->
                viewModel.onAction(ExpenseFormAction.OnNameChange(value))
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
    }
}