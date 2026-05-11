package com.macrobytes.balancify.presentation.expense_detail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.macrobytes.balancify.component.AppBar
import com.macrobytes.balancify.presentation.expense_detail.ExpenseDetailAction
import com.macrobytes.balancify.presentation.expense_detail.ExpenseDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpenseDetailAppBar(
    viewModel: ExpenseDetailViewModel = koinViewModel(),
    onNavigateToExpenseFrom: (id: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    AppBar("Expense Detail", onBackClick) {
        if (!state.value.isCreateByLocalUser)
            return@AppBar

        IconButton(
            enabled = state.value.enableAllAction,
            onClick = {
                viewModel.onAction(ExpenseDetailAction.OnDropdownMenuToggle)
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "More options"
            )
        }
        DropdownMenu(
            expanded = state.value.showDropdown,
            onDismissRequest = {
                viewModel.onAction(ExpenseDetailAction.OnDropdownMenuToggle)
            },
        ) {
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("Edit")
                    }
                },
                enabled = state.value.enableAllAction,
                onClick = {
                    viewModel.onAction(ExpenseDetailAction.OnDropdownMenuToggle)
                    onNavigateToExpenseFrom(state.value.expense.id)
                }
            )
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("Delete")
                    }
                },
                enabled = state.value.enableAllAction,
                onClick = {
                    viewModel.onAction(ExpenseDetailAction.OnDeleteBottomSheetToggle)
                }
            )
        }
    }
}