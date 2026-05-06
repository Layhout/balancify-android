package com.example.balancify.presentation.home.component.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import com.example.balancify.component.CardOrder
import com.example.balancify.component.Empty
import com.example.balancify.component.StyledCard
import com.example.balancify.core.ext.getCurrencyFormatted
import com.example.balancify.domain.model.ExpenseIcon
import com.example.balancify.presentation.home.component.dashboard.component.SpendingGraph
import com.example.balancify.presentation.home.component.dashboard.component.SummaryCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
    onNavigateToExpenseDetail: (String) -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        PullToRefreshBox(
            isRefreshing = state.value.isRefreshing,
            onRefresh = {
                viewModel.onAction(DashboardAction.OnRefresh)
            },
        ) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
            ) {
                item {
                    SummaryCard()
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    SpendingGraph()
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Text(
                        "Recent",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (state.value.data?.expenses?.isEmpty() ?: true)
                        Empty()
                }
                itemsIndexed(
                    items = (state.value.data?.expenses ?: emptyList()).take(5)
                ) { index, item ->
                    if (index != 0) Spacer(modifier = Modifier.height(2.dp))

                    StyledCard(
                        order = CardOrder.fromIndexAndSize(
                            index,
                            (state.value.data?.expenses?.size ?: 0).coerceAtMost(5)
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable(onClick = {
                                    onNavigateToExpenseDetail(item.id)
                                })
                                .padding(16.dp),
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
                                    "Paid by ${item.getPayerName(state.value.localUser?.id ?: "")}",
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
                    }

                }
            }
        }
    }
}