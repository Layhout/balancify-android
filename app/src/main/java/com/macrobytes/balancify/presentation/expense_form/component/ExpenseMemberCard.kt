package com.macrobytes.balancify.presentation.expense_form.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FrontHand
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.macrobytes.balancify.component.CardOrder
import com.macrobytes.balancify.component.DropdownOption
import com.macrobytes.balancify.component.UserListCard
import com.macrobytes.balancify.core.ext.formatAmountTextFieldValue
import com.macrobytes.balancify.core.ext.formatAmountValueChange
import com.macrobytes.balancify.core.ext.toCleanString
import com.macrobytes.balancify.domain.model.ExpenseMemberModel
import com.macrobytes.balancify.domain.model.MemberOption
import com.macrobytes.balancify.domain.model.SplitOption
import com.macrobytes.balancify.domain.model.UserModel
import com.macrobytes.balancify.presentation.expense_form.ExpenseFormAction
import com.macrobytes.balancify.presentation.expense_form.ExpenseFormViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExpenseMemberCard(
    viewModel: ExpenseFormViewModel = koinViewModel(),
    data: ExpenseMemberModel,
    order: CardOrder,
    index: Int,
) {
    var displayAmount by remember { mutableStateOf("") }
    val state = viewModel.state.collectAsStateWithLifecycle()

    val isCustomAmount = state.value.splitOption == SplitOption.CUSTOM

    LaunchedEffect(state.value.splitOption) {
        if (state.value.splitOption == SplitOption.CUSTOM) {
            displayAmount = data.amount.toCleanString()
        }
    }

    UserListCard(
        order = order,
        user = UserModel(
            name = data.name,
            imageUrl = data.imageUrl,
        ),
        subTitleContent = {
            if (data.id == state.value.paidBy?.id)
                Text(
                    "pays for this expense",
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary)
                )
        },
        action = {
            BasicTextField(
                enabled = isCustomAmount,
                value = formatAmountTextFieldValue(
                    if (isCustomAmount) displayAmount
                    else data.amount.toCleanString()
                ),
                onValueChange = { newValue ->
                    val newValueString = newValue.formatAmountValueChange() ?: return@BasicTextField
                    displayAmount = newValueString
                    viewModel.onAction(
                        ExpenseFormAction.OnMemberAmountChange(
                            index,
                            newValueString
                        )
                    )
                },
                textStyle = MaterialTheme.typography.labelMedium.copy(
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .width(70.dp)
                    .height(32.dp)
                    .background(MaterialTheme.colorScheme.outline)
                    .padding(bottom = 1.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,
                    autoCorrectEnabled = false,
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        if (data.amount.toCleanString().isEmpty()) {
                            Text(
                                text = "$0.00",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    textAlign = TextAlign.End,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                        innerTextField()
                    }
                },
            )

            DropdownOption {
                DropdownMenuItem(
                    enabled = state.value.isEnableAllAction || state.value.paidBy?.id != data.id,
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.FrontHand,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(12.dp))
                            Text("Assign payer")
                        }
                    },
                    onClick = {
                        viewModel.onAction(ExpenseFormAction.OnPaidByChange(data.id))
                    },
                )
                if (state.value.memberOption == MemberOption.FRIEND)
                    DropdownMenuItem(
                        enabled = state.value.isEnableAllAction,
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Outlined.PersonRemove,
                                    tint = MaterialTheme.colorScheme.error,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(12.dp))
                                Text("Remove")
                            }
                        },
                        onClick = {
                            viewModel.onAction(ExpenseFormAction.OnMemberRemove(index))
                        },
                    )
            }
        }
    )
}