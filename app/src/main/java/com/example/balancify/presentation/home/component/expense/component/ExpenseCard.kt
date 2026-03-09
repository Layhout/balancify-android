package com.example.balancify.presentation.home.component.expense.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import com.example.balancify.component.Avatar
import com.example.balancify.component.StackAvatar
import com.example.balancify.component.StyledCard
import com.example.balancify.core.constant.BG_COLORS
import com.example.balancify.core.ext.darken
import com.example.balancify.core.ext.getCurrencyFormatted
import com.example.balancify.domain.model.ExpenseIcon
import com.example.balancify.domain.model.ExpenseModel
import com.example.balancify.presentation.home.component.expense.ExpenseViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseCard(
    viewModel: ExpenseViewModel = koinViewModel(),
    item: ExpenseModel,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    StyledCard(modifier = Modifier.padding(0.dp)) {
        Column(
            modifier = Modifier
                .clickable(onClick = {})
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = Color(item.iconBgColor.toColorInt()),
                            shape = RoundedCornerShape(6.dp)
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        ImageVector.vectorResource(
                            ExpenseIcon.getIconIdFromValue(item.icon)
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        item.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        "Paid by ${item.getPayerName(state.value.localUser?.id)}",
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    item.amount.getCurrencyFormatted(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black
                    )
                )
            }
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        val members = item.member.values.toList()
                        StackAvatar {
                            members.take(3).forEach {
                                Avatar(
                                    imageUrl = it.imageUrl,
                                    modifier = Modifier.size(32.dp),
                                    fallbackText = it.name,
                                    bgColor = Color(BG_COLORS[0].toColorInt()),
                                )
                            }
                            if (members.size > 5)
                                Avatar(
                                    imageUrl = "",
                                    modifier = Modifier.size(32.dp),
                                    fallbackText = "+ ${members.size - 5}",
                                    bgColor = MaterialTheme.colorScheme.outlineVariant,
                                )
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        val status = item.getSettlementStatus()

                        Text(
                            "${item.getSettlePercentage()}%",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black
                            )
                        )
                        Text(
                            status,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = when (status) {
                                    "Settled" -> Color.Green.darken(0.5f)
                                    "Overpaid" -> Color.Red.darken(0.5f)
                                    else -> MaterialTheme.colorScheme.onBackground
                                },
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                val createdDate: String? = item.createdAt
                    ?.toInstant()
                    ?.atZone(ZoneId.systemDefault())
                    ?.toLocalDate()?.format(
                        DateTimeFormatter.ofPattern("dd MMM yyyy")
                    )

                Row {
                    Text(
                        createdDate ?: "",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}