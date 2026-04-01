package com.example.balancify.presentation.group_detail

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.ConfirmationBottomSheet
import com.example.balancify.component.ExpenseCard
import com.example.balancify.component.InfiniteLazyColumn
import com.example.balancify.core.util.ObserveAsEvents
import com.example.balancify.presentation.group_detail.component.GroupDetailAppBar
import com.example.balancify.presentation.group_detail.component.GroupDetailFooter
import com.example.balancify.presentation.group_detail.component.GroupDetailHeader
import com.example.balancify.presentation.group_detail.component.GroupMemberBottomSheet
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupDetailScreen(
    viewModel: GroupDetailViewModel = koinViewModel(),
    onNavigateToGroupFrom: (String) -> Unit,
    onNavigateToExpenseDetail: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAction(GroupDetailAction.OnCollectFlag)

    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is GroupDetailEvent.OnError -> {
                Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            GroupDetailEvent.OnLeaveGroup -> onBackClick()
        }
    }

    Surface(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.background
        )
    ) {
        Scaffold(
            topBar = {
                GroupDetailAppBar(
                    onNavigateToGroupFrom = onNavigateToGroupFrom,
                    onBackClick = onBackClick
                )
            },
        ) {
            Column(modifier = Modifier.padding(it)) {
                PullToRefreshBox(
                    isRefreshing = state.value.isRefreshing,
                    onRefresh = { viewModel.onAction(GroupDetailAction.OnRefresh) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                ) {
                    InfiniteLazyColumn(
                        header = {
                            GroupDetailHeader()
                        },
                        items = state.value.expenses,
                        isLoadingMore = state.value.isLoading,
                        canLoadMore = state.value.canLoadMore,
                        onLoadMore = {
                            viewModel.onAction(GroupDetailAction.OnLoadMore)
                        },
                        modifier = Modifier
                            .fillMaxSize(),
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
                GroupDetailFooter()
            }

            if (state.value.showMemberBottomSheet)
                GroupMemberBottomSheet()
            if (state.value.isLeaveBottomSheetVisible)
                ConfirmationBottomSheet(
                    message = "Are you sure you want to leave this group?",
                    confirmText = "Leave",
                    onConfirmClick = { viewModel.onAction(GroupDetailAction.OnLeaveDismiss) },
                    onDismissRequest = { viewModel.onAction(GroupDetailAction.OnLeaveDismiss) },
                )
        }
    }
}