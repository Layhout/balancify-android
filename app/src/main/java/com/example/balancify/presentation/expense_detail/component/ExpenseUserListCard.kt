package com.example.balancify.presentation.expense_detail.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.balancify.component.CardOrder
import com.example.balancify.component.UserListCard
import com.example.balancify.core.ext.getCurrencyFormatted
import com.example.balancify.domain.model.ExpenseMemberModel
import com.example.balancify.domain.model.UserModel
import kotlin.math.abs

@JvmOverloads
@Composable
fun ExpenseUserListCard(
    data: ExpenseMemberModel,
    order: CardOrder = CardOrder.ALONE,
    localUserId: String,
    payerId: String,
) {
    val balance = data.amount - data.settledAmount

    val settlementStatus = when {
        balance < 0.0 -> "Overpaid"
        balance == 0.0 -> "Settled"
        else -> "Owns"
    }

    UserListCard(
        user = UserModel(
            imageUrl = data.imageUrl,
            name = "${data.name} ${if (data.id == localUserId) "(You)" else ""}",
            id = data.id,
        ),
        order = order,
        subTitleContent = {
            Text(
                "${if (payerId == data.id) "Payer ·" else ""} ${
                    when (settlementStatus) {
                        "Overpaid" -> "$settlementStatus ${abs(balance).getCurrencyFormatted()}"
                        "Settled" -> settlementStatus
                        else -> "$settlementStatus ${balance.getCurrencyFormatted()}"
                    }
                }",
                style = MaterialTheme.typography.labelMedium
            )
        }
    )
}