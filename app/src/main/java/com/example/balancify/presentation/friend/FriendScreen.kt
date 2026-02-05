package com.example.balancify.presentation.friend

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.Empty
import com.example.balancify.component.InfiniteLazyColumn
import com.example.balancify.core.constant.BORDER_RADIUS_MD
import com.example.balancify.core.constant.BORDER_RADIUS_SM
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
        }
    }

    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Friends", fontWeight = FontWeight.SemiBold)
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
                        shape =
                            if (state.value.friends.size == 1) {
                                RoundedCornerShape(BORDER_RADIUS_MD)
                            } else {
                                when (index) {
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