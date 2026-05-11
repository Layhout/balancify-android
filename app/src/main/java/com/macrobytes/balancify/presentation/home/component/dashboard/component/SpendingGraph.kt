package com.macrobytes.balancify.presentation.home.component.dashboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.macrobytes.balancify.component.Bar
import com.macrobytes.balancify.component.BarGraph
import com.macrobytes.balancify.component.BarType
import com.macrobytes.balancify.component.StyledCard
import com.macrobytes.balancify.core.ext.getCurrencyFormatted
import com.macrobytes.balancify.presentation.home.component.dashboard.DashboardViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SpendingGraph(
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    StyledCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                "Spending History",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(32.dp))
            if (state.value.isLoading)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Loading...", style = MaterialTheme.typography.labelSmall)
                }
            else
                BarGraph(
                    bars = state.value.data?.spendingHistory?.map {
                        Bar(
                            value = it.second.toFloat(),
                            label = it.first,
                        )
                    } ?: emptyList(),
                    height = 300.dp,
                    roundType = BarType.TOP_CURVED,
                    barWidth = 70.dp,
                    barColor = MaterialTheme.colorScheme.primary,
                    barArrangement = Arrangement.SpaceEvenly,
                    valueLabelFormater = {
                        it.toDouble().getCurrencyFormatted()
                    }
                )
        }
    }
}