package com.macrobytes.balancify.presentation.expense_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DataSaverOn
import androidx.compose.material.icons.outlined.PeopleAlt
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.macrobytes.balancify.presentation.expense_detail.ExpenseDetailAction
import com.macrobytes.balancify.presentation.expense_detail.ExpenseDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpenseDetailFooter(
    viewModel: ExpenseDetailViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Row(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                viewModel.onAction(ExpenseDetailAction.OnSettlementBottomSheetToggle)
            },
            modifier = Modifier.weight(1f),
            enabled = state.value.enableAllAction && !state.value.isAlreadySettled,
        ) {
            Icon(Icons.Outlined.DataSaverOn, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Settle")
        }
        FilledIconButton(
            enabled = state.value.enableAllAction,
            onClick = {
                viewModel.onAction(ExpenseDetailAction.OnMemberBottomSheetToggle)
            },
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
        ) {
            Icon(Icons.Outlined.PeopleAlt, contentDescription = null)
        }
    }
}