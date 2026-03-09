package com.example.balancify.presentation.home.component.expense

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.Empty
import com.example.balancify.component.InfiniteLazyColumn
import com.example.balancify.presentation.home.component.expense.component.ExpenseCard
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        PullToRefreshBox(
            isRefreshing = state.value.isRefreshing,
            onRefresh = { viewModel.onAction(ExpenseAction.OnRefresh) },
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            if (!state.value.isLoading && state.value.expenses.isEmpty()) {
                Empty()
            }

            InfiniteLazyColumn(
                items = state.value.expenses,
                isLoadingMore = state.value.isLoading,
                canLoadMore = state.value.canLoadMore,
                onLoadMore = {
                    viewModel.onAction(ExpenseAction.OnLoadMore)
                },
                modifier = Modifier
                    .fillMaxSize(),
            ) { index, item ->
                if (index != 0) Spacer(modifier = Modifier.height(8.dp))

                ExpenseCard(item = item)
            }
        }
    }
}