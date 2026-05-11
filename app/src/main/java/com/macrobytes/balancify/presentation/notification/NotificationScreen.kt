package com.macrobytes.balancify.presentation.notification

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.macrobytes.balancify.component.AppBar
import com.macrobytes.balancify.component.CardOrder
import com.macrobytes.balancify.component.Empty
import com.macrobytes.balancify.component.InfiniteLazyColumn
import com.macrobytes.balancify.core.util.ObserveAsEvents
import com.macrobytes.balancify.domain.model.NotificationType
import com.macrobytes.balancify.presentation.notification.component.NotificationCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = koinViewModel(),
    onNavigateToFriend: () -> Unit,
    onNavigateToExpenseDetail: (String) -> Unit,
    onNavigateToGroupDetail: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is NotificationEvent.OnError -> {
                Toast.makeText(
                    context, event.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        }

    }

    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Scaffold(
            topBar = {
                AppBar("Notification", onBackClick)
            },
        ) {
            PullToRefreshBox(
                isRefreshing = state.value.isRefreshing,
                onRefresh = { viewModel.onAction(NotificationAction.OnRefresh) },
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp),
            ) {

                if (!state.value.isLoading && state.value.notifications.isEmpty()) {
                    Empty()
                }
                InfiniteLazyColumn(
                    items = state.value.notifications,
                    isLoadingMore = state.value.isLoading,
                    canLoadMore = state.value.canLoadMore,
                    contentPadding = PaddingValues(bottom = 16.dp),
                    onLoadMore = {
                        viewModel.onAction(NotificationAction.OnLoadMore)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                ) { index, item ->
                    if (index != 0) Spacer(modifier = Modifier.height(2.dp))

                    NotificationCard(
                        item,
                        onClick = {
                            when (item.type) {
                                NotificationType.FRIEND_REQUEST -> onNavigateToFriend()
                                NotificationType.EXPENSE -> {
                                    val id = item.link?.split("/")?.last() ?: ""
                                    if (id.isNotEmpty()) onNavigateToExpenseDetail(id)
                                }

                                NotificationType.GROUP -> {
                                    val id = item.link?.split("/")?.last() ?: ""
                                    if (id.isNotEmpty()) onNavigateToGroupDetail(id)
                                }
                            }
                        },
                        order = CardOrder.fromIndexAndSize(
                            index,
                            state.value.notifications.size
                        ),
                    )
                }
            }
        }
    }
}