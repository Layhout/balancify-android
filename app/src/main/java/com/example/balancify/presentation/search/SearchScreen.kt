package com.example.balancify.presentation.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.AppBar
import com.example.balancify.component.CardOrder
import com.example.balancify.component.Empty
import com.example.balancify.component.InfiniteLazyColumn
import com.example.balancify.component.UserListCard
import com.example.balancify.core.constant.SearchResult
import com.example.balancify.core.constant.SearchType
import com.example.balancify.core.util.ObserveAsEvents
import com.example.balancify.domain.model.FoundItemData
import com.example.balancify.domain.model.UserModel
import com.example.balancify.presentation.search.component.SearchTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    onResultSelected: (SearchResult) -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

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
                    "Search ${if (state.value.searchType == SearchType.FRIEND) "Friend" else "Group"}",
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
                        Empty(
                            emptyText = "No Data. Search Someone."
                        )
                    }

                    InfiniteLazyColumn(
                        items = state.value.foundItems,
                        isLoadingMore = state.value.isLoading,
                        canLoadMore = state.value.canLoadMore,
                        onLoadMore = {
                            viewModel.onAction(SearchAction.OnLoadMore)
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { index, item ->
                        if (index != 0) Spacer(modifier = Modifier.height(2.dp))

                        if (state.value.searchType == SearchType.FRIEND) {
                            UserListCard(
                                modifier = Modifier.clickable(
                                    onClick = {
                                        onResultSelected(
                                            SearchResult.Friend(
                                                (item.data as FoundItemData.Friend).data
                                            )
                                        )
                                    }
                                ),
                                order = CardOrder.getOrderFrom(index, state.value.foundItems.size),
                                user = (item.data as FoundItemData.Friend).data.user ?: UserModel()
                            )
                        }
                    }
                }
            }
        }
    }
}