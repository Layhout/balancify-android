package com.example.balancify.presentation.home.component.group

import android.os.Build
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.Empty
import com.example.balancify.component.InfiniteLazyColumn
import com.example.balancify.core.util.ObserveAsEvents
import com.example.balancify.presentation.home.component.group.component.GroupCard
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupScreen(
    viewModel: GroupViewModel = koinViewModel(),
    onNavigateToGroupDetail: (String) -> Unit,
    shouldRefreshGroupList: Boolean = false
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(shouldRefreshGroupList) {
        if (shouldRefreshGroupList) viewModel.onAction(GroupAction.OnRefresh)
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is GroupEvent.OnError -> {
                Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    Surface(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.background
        )
    ) {
        PullToRefreshBox(
            isRefreshing = state.value.isRefreshing,
            onRefresh = { viewModel.onAction(GroupAction.OnRefresh) },
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            if (!state.value.isLoading && state.value.groups.isEmpty()) {
                Empty()
            }

            InfiniteLazyColumn(
                items = state.value.groups,
                isLoadingMore = state.value.isLoading,
                canLoadMore = state.value.canLoadMore,
                onLoadMore = {
                    viewModel.onAction(GroupAction.OnLoadMore)
                },
                modifier = Modifier
                    .fillMaxSize(),
            ) { index, item ->
                if (index != 0) Spacer(modifier = Modifier.height(2.dp))

                GroupCard(
                    index = index,
                    item = item,
                    onClick = {
                        onNavigateToGroupDetail(item.id)
                    }
                )
            }
        }
    }
}
