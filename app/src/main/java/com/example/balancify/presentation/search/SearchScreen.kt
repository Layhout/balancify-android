package com.example.balancify.presentation.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.AppBar
import com.example.balancify.component.Avatar
import com.example.balancify.component.CardOrder
import com.example.balancify.component.Empty
import com.example.balancify.component.InfiniteLazyColumn
import com.example.balancify.component.StyledCard
import com.example.balancify.core.constant.SearchType
import com.example.balancify.core.util.ObserveAsEvents
import com.example.balancify.presentation.search.component.SearchTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    type: SearchType,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAction(SearchAction.OnSearchTypeReceive(type))
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SearchEvent.OnError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Scaffold(
            topBar = {
                AppBar(
                    "Search ${if (type == SearchType.FRIEND) "Friend" else "Group"}",
                    onBackClick
                )
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 16.dp),
            ) {
                SearchTextField { searchValue ->
                    viewModel.onAction(SearchAction.OnSearchClick(searchValue))
                }
                Spacer(modifier = Modifier.height(16.dp))
                PullToRefreshBox(
                    isRefreshing = state.value.isRefreshing,
                    onRefresh = { viewModel.onAction(SearchAction.OnRefresh) },
                ) {
                    if (!state.value.isLoading && state.value.foundItems.isEmpty()) {
                        Empty()
                    }
                    
                    InfiniteLazyColumn(
                        items = state.value.foundItems,
                        isLoadingMore = state.value.isLoading,
                        canLoadMore = state.value.canLoadMore,
                        onLoadMore = {
                            viewModel.onAction(SearchAction.OnLoadMore)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) { index, item ->
                        if (index != 0) Spacer(modifier = Modifier.height(2.dp))

                        StyledCard(
                            order =
                                if (state.value.foundItems.size == 1) {
                                    CardOrder.ALONE
                                } else {
                                    when (index) {
                                        0 -> CardOrder.FIRST
                                        state.value.foundItems.size - 1 -> CardOrder.LAST
                                        else -> CardOrder.MIDDLE
                                    }
                                },
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (item.imageUrl.isNotBlank())
                                    Avatar(
                                        imageUrl = item.imageUrl,
                                        modifier = Modifier.size(42.dp)
                                    )
                                Text(
                                    item.name,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}