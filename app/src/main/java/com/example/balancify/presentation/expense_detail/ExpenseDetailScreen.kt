package com.example.balancify.presentation.expense_detail

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.ConfirmationBottomSheet
import com.example.balancify.core.util.ObserveAsEvents
import com.example.balancify.presentation.expense_detail.component.ExpenseDetailAppBar
import com.example.balancify.presentation.expense_detail.component.ExpenseDetailFooter
import com.example.balancify.presentation.expense_detail.component.ExpenseDetailHeader
import com.example.balancify.presentation.expense_detail.component.ExpenseMemberBottomSheet
import com.example.balancify.presentation.expense_detail.component.SettlementBottomSheet
import com.example.balancify.presentation.expense_detail.component.TimelineItem
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseDetailScreen(
    viewModel: ExpenseDetailViewModel = koinViewModel(),
    onNavigateToExpenseFrom: (id: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onAction(ExpenseDetailAction.OnCollectFlag)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ExpenseDetailEvent.OnError -> {
                Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            ExpenseDetailEvent.OnDeletionSuccess -> {
                onBackClick()
            }
        }
    }

    Surface(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.background
        )
    ) {
        Scaffold(
            topBar = {
                ExpenseDetailAppBar(
                    onNavigateToExpenseFrom = onNavigateToExpenseFrom,
                    onBackClick = onBackClick
                )
            },
        ) {
            Column(modifier = Modifier.padding(it)) {
                PullToRefreshBox(
                    isRefreshing = state.value.isRefreshing,
                    onRefresh = { viewModel.onAction(ExpenseDetailAction.OnRefresh) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    if (state.value.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                        return@PullToRefreshBox
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 6.dp)
                    ) {
                        item {
                            ExpenseDetailHeader()
                        }
                        itemsIndexed(items = state.value.expense.timelines) { index, item ->
                            TimelineItem(
                                data = item,
                                isFirst = index == 0,
                                isLast = index == state.value.expense.timelines.lastIndex,
                            )
                        }
                    }
                }
                ExpenseDetailFooter()
                if (state.value.showMemberBottomSheet)
                    ExpenseMemberBottomSheet()
                if (state.value.showDeleteConfirmationBottomSheet)
                    ConfirmationBottomSheet(
                        message = "Are you sure you want to delete this expense?",
                        confirmText = "Delete",
                        onConfirmClick = {
                            viewModel.onAction(
                                ExpenseDetailAction.OnConfirmDeletion
                            )
                        },
                        onDismissRequest = {
                            viewModel.onAction(
                                ExpenseDetailAction.OnDeleteBottomSheetToggle
                            )
                        },
                    )
                if (state.value.showSettlementBottomSheet)
                    SettlementBottomSheet()
            }
        }
    }
}