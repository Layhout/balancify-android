package com.example.balancify.presentation.expense_form.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.ToggleButtonGroup
import com.example.balancify.domain.model.MemberOption
import com.example.balancify.domain.model.SplitOption
import com.example.balancify.presentation.expense_form.ExpenseFormAction
import com.example.balancify.presentation.expense_form.ExpenseFormViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpenseSetting(
    viewModel: ExpenseFormViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "This expense is shared with:",
                style = MaterialTheme.typography.labelLarge
            )
            ToggleButtonGroup(
                enabled = state.value.isEnableAllAction,
                options = MemberOption.entries.map { option -> option.label },
                selectedIndex = state.value.memberOption.ordinal,
                onSelectedIndexChange = { index ->
                    viewModel.onAction(
                        ExpenseFormAction.OnMemberOptionChange(
                            MemberOption.entries[index]
                        )
                    )
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "We split it:",
                style = MaterialTheme.typography.labelLarge
            )
            ToggleButtonGroup(
                enabled = state.value.isEnableAllAction,
                options = SplitOption.entries.map { option -> option.label },
                selectedIndex = state.value.splitOption.ordinal,
                onSelectedIndexChange = { index ->
                    viewModel.onAction(
                        ExpenseFormAction.OnSplitOptionChange(
                            SplitOption.entries[index]
                        )
                    )
                }
            )
        }
    }
}