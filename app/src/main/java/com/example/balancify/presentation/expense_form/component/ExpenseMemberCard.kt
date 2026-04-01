package com.example.balancify.presentation.expense_form.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.CardOrder
import com.example.balancify.component.UserListCard
import com.example.balancify.domain.model.ExpenseMemberModel
import com.example.balancify.domain.model.MemberOption
import com.example.balancify.domain.model.SplitOption
import com.example.balancify.domain.model.UserModel
import com.example.balancify.presentation.expense_form.ExpenseFormAction
import com.example.balancify.presentation.expense_form.ExpenseFormViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExpenseMemberCard(
    viewModel: ExpenseFormViewModel = koinViewModel(),
    data: ExpenseMemberModel,
    order: CardOrder,
    index: Int,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    UserListCard(
        order = order,
        user = UserModel(
            name = data.name,
            imageUrl = data.imageUrl,
        ),
        subTitleContent = {},
        action = {
            BasicTextField(
                enabled = state.value.splitOption == SplitOption.CUSTOM,
                value = data.amount.toString(),
                onValueChange = { amount ->
                    viewModel.onAction(ExpenseFormAction.OnMemberAmountChanged(index, amount))
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.AttachMoney,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                        ) {
                            innerTextField()
                        }
                    }
                }
            )
            if (state.value.memberOption == MemberOption.FRIEND)
                IconButton(
                    modifier = Modifier.size(38.dp),
                    onClick = {
                        viewModel.onAction(ExpenseFormAction.OnMemberRemoved(index))
                    }
                ) {
                    Icon(
                        Icons.Outlined.PersonRemove,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = null
                    )
                }
        }
    )
}