package com.example.balancify.presentation.expense_detail.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.balancify.component.CardOrder
import com.example.balancify.component.UserListCard
import com.example.balancify.core.ext.darken
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
            if (payerId == data.id)
                Text("Payer", style = MaterialTheme.typography.labelMedium)
            else
                when (settlementStatus) {
                    "Overpaid" -> {
                        Text(
                            "$settlementStatus ${abs(balance).getCurrencyFormatted()}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.Red.darken(0.5f)
                            )
                        )
                    }

                    "Settled" -> {
                        Text(
                            settlementStatus,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.Green.darken(0.5f)
                            )
                        )
                    }

                    "Owns" -> {
                        Text(
                            "$settlementStatus ${balance.getCurrencyFormatted()}",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
        }
    )
}