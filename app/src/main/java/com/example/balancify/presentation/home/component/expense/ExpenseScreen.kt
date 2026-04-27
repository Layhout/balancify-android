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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.Empty
import com.example.balancify.component.ExpenseCard
import com.example.balancify.component.InfiniteLazyColumn
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel = koinViewModel(),
    onNavigateToExpenseDetail: (String) -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onAction(ExpenseAction.OnCollectFlag)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
            ) { index, item ->
                if (index != 0) Spacer(modifier = Modifier.height(8.dp))

                ExpenseCard(
                    item = item,
                    localUserId = state.value.localUser?.id ?: "",
                    onClick = {
                        onNavigateToExpenseDetail(item.id)
                    }
                )
            }
        }
    }
}