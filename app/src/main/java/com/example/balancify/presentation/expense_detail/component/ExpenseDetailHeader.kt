package com.example.balancify.presentation.expense_detail.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.StyledCard
import com.example.balancify.core.ext.format
import com.example.balancify.core.ext.getCurrencyFormatted
import com.example.balancify.domain.model.ExpenseIcon
import com.example.balancify.presentation.expense_detail.ExpenseDetailViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseDetailHeader(
    viewModel: ExpenseDetailViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val expense = state.value.expense

    Column(modifier = Modifier.fillMaxWidth()) {
        StyledCard(
            colors = CardDefaults.cardColors(
                containerColor = Color(expense.iconBgColor.toColorInt()).copy(alpha = 0.2f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    ImageVector.vectorResource(
                        ExpenseIcon.getIconIdFromValue(expense.icon)
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(52.dp),
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    expense.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "Paid By ${
                        if (state.value.isPaidByLocalUser)
                            "You"
                        else
                            expense.paidBy.name
                    } • ${expense.createdAt?.format("dd MMM yyyy") ?: ""}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    expense.amount.getCurrencyFormatted(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.W900
                    )
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("You Owed", style = MaterialTheme.typography.labelSmall)
                        Text(
                            expense.getLocalUserOweAmount(
                                state.value.localUser?.id ?: ""
                            )
                                .getCurrencyFormatted(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Settled", style = MaterialTheme.typography.labelSmall)
                        Text(
                            "${expense.getSettlePercentage()}%",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            "Timeline",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(12.dp))
    }
}