package com.example.balancify.presentation.friend

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.InfiniteLazyColumn
import com.example.balancify.core.constant.BORDER_RADIUS_MD
import com.example.balancify.core.constant.BORDER_RADIUS_SM
import com.example.balancify.core.util.ObserveAsEvents
import com.example.balancify.presentation.friend.component.FriendCard
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendScreen(
    viewModel: FriendViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is FriendEvent.OnError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Friends")
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { }) {
                    Icon(
                        Icons.Rounded.Add,
                        "New Expense"
                    )
                }
            }
        ) {
            PullToRefreshBox(
                isRefreshing = false,
                onRefresh = {},
                modifier = Modifier
                    .padding(it),
            ) {
                if (!state.value.isLoading && state.value.friends.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(34.dp))
                        Text(
                            "(-_-;)",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Black
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No friends found.")
                    }
                }
                InfiniteLazyColumn(
                    items = state.value.friends,
                    isLoadingMore = state.value.isLoading,
                    canLoadMore = state.value.canLoadMore,
                    onLoadMore = {
                        viewModel.onAction(FriendAction.OnLoadMore)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) { index, item ->

                    if (index != 0) Spacer(modifier = Modifier.height(2.dp))
                    FriendCard(
                        shape = when (index) {
                            0 -> RoundedCornerShape(
                                topStart = BORDER_RADIUS_MD,
                                topEnd = BORDER_RADIUS_MD,
                                bottomStart = BORDER_RADIUS_SM,
                                bottomEnd = BORDER_RADIUS_SM,
                            )

                            state.value.friends.size - 1 -> {
                                RoundedCornerShape(
                                    topStart = BORDER_RADIUS_SM,
                                    topEnd = BORDER_RADIUS_SM,
                                    bottomStart = BORDER_RADIUS_MD,
                                    bottomEnd = BORDER_RADIUS_MD,
                                )
                            }

                            else -> RoundedCornerShape(BORDER_RADIUS_SM)
                        },
                        data = item
                    )
                }
            }
        }
    }
}