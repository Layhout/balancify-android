package com.example.balancify.presentation.friend

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.example.balancify.core.util.ObserveAsEvents
import com.example.balancify.presentation.friend.component.AddFriendDialog
import com.example.balancify.presentation.friend.component.FriendCard
import com.example.balancify.presentation.friend.component.InviteLinkBottomSheet
import com.example.balancify.presentation.friend.component.UnfriendConfirmationDialog
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

            is FriendEvent.OnShareLinkClicked -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, state.value.inviteLink)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            }
        }
    }

    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Scaffold(
            topBar = {
                AppBar("Friends", onBackClick)
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.onAction(FriendAction.OnAddFriendClick)
                }) {
                    Icon(
                        Icons.Rounded.Add,
                        "New Expense"
                    )
                }
            }
        ) {
            PullToRefreshBox(
                isRefreshing = state.value.isRefreshing,
                onRefresh = { viewModel.onAction(FriendAction.OnRefresh) },
                modifier = Modifier
                    .padding(it),
            ) {
                if (!state.value.isLoading && state.value.friends.isEmpty()) {
                    Empty()
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
                        data = item,
                        order =
                            if (state.value.friends.size == 1) {
                                CardOrder.ALONE
                            } else {
                                when (index) {
                                    0 -> CardOrder.FIRST
                                    state.value.friends.size - 1 -> CardOrder.LAST
                                    else -> CardOrder.MIDDLE
                                }
                            },
                    )
                }
            }
        }

        if (state.value.unfriendConfirmationDialogVisible)
            UnfriendConfirmationDialog()
        if (state.value.addFriendDialogVisible)
            AddFriendDialog()
        if (state.value.inviteLinkBottomSheetVisible)
            InviteLinkBottomSheet()
    }
}