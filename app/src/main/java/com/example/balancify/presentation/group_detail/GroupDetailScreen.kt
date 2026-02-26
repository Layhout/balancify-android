package com.example.balancify.presentation.group_detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.AppBar
import com.example.balancify.component.InfiniteLazyColumn
import com.example.balancify.core.util.ObserveAsEvents
import com.example.balancify.presentation.group_detail.component.DetailHeader
import com.example.balancify.presentation.group_detail.component.MemberBottomSheet
import org.koin.androidx.compose.koinViewModel

@Composable
fun GroupDetailScreen(
    viewModel: GroupDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is GroupDetailEvent.OnError -> Toast.makeText(
                context,
                event.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Surface(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.background
        )
    ) {
        Scaffold(
            topBar = {
                AppBar("Group Detail", onBackClick) {
                    IconButton(
                        onClick = {
                            viewModel.onAction(GroupDetailAction.OnDropdownMenuToggle)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "More options"
                        )
                    }

                    // DropdownMenu content
                    DropdownMenu(
                        expanded = state.value.showDropdown,
                        onDismissRequest = {
                            viewModel.onAction(GroupDetailAction.OnDropdownMenuToggle)
                        },
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Outlined.Edit,
                                        contentDescription = null
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text("Edit")
                                }
                            },
                            onClick = {
                                viewModel.onAction(GroupDetailAction.OnDropdownMenuToggle)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Outlined.Logout,
                                        contentDescription = null
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text("Leave")
                                }
                            },
                            onClick = {
                                viewModel.onAction(GroupDetailAction.OnDropdownMenuToggle)
                            }
                        )
                    }
                }
            },
        ) {
            PullToRefreshBox(
                isRefreshing = state.value.isRefreshing,
                onRefresh = { viewModel.onAction(GroupDetailAction.OnRefresh) },
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp),
            ) {
                InfiniteLazyColumn(
                    header = {
                        DetailHeader()
                    },
                    items = emptyList<Any>(),
                    isLoadingMore = state.value.isLoading,
                    canLoadMore = state.value.canLoadMore,
                    onLoadMore = {
                        viewModel.onAction(GroupDetailAction.OnLoadMore)
                    },
                    modifier = Modifier
                        .fillMaxSize(),
                ) { index, item -> }
            }

            if (state.value.showMemberBottomSheet)
                MemberBottomSheet()
        }
    }
}