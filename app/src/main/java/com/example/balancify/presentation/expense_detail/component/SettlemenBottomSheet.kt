package com.example.balancify.presentation.expense_detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.East
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.AmountInputField
import com.example.balancify.component.Avatar
import com.example.balancify.presentation.expense_detail.ExpenseDetailAction
import com.example.balancify.presentation.expense_detail.ExpenseDetailViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettlementBottomSheet(
    viewModel: ExpenseDetailViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            viewModel.onAction(ExpenseDetailAction.OnSettlementBottomSheetToggle)
        },
        sheetState = sheetState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 0.dp),
        ) {
            Text("Record Settlement", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(2.dp))
            Text(
                "Use negative amount to settle overpaid.",
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Avatar(
                    imageUrl = state.value.localUser?.imageUrl ?: "",
                    fallbackText = state.value.localUser?.name ?: "",
                    bgColor = Color(state.value.localUser?.profileBgColor?.toColorInt() ?: 0),
                    modifier = Modifier.size(80.dp),
                )
                Spacer(Modifier.width(24.dp))
                Icon(
                    Icons.Outlined.East,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                )
                Spacer(Modifier.width(24.dp))
                Avatar(
                    imageUrl = state.value.expense.paidBy.imageUrl,
                    fallbackText = state.value.expense.paidBy.name,
                    bgColor = Color(state.value.expense.paidBy.profileBgColor.toColorInt()),
                    modifier = Modifier.size(80.dp),
                )
            }
            Spacer(Modifier.height(16.dp))
            AmountInputField(
                amount = state.value.settlementAmount,
                onAmountChange = {
                    viewModel.onAction(ExpenseDetailAction.OnSettlementAmountChange(it))
                }
            )
            Spacer(Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.onAction(ExpenseDetailAction.OnSettlementSubmit)
                },
                enabled = state.value.enableAllAction
            ) {
                if (state.value.isSettling) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text("Submit")
            }
        }
    }
}