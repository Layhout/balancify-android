package com.example.balancify.presentation.group_detail.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.CardOrder
import com.example.balancify.component.UserListCard
import com.example.balancify.presentation.group_detail.GroupDetailAction
import com.example.balancify.presentation.group_detail.GroupDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberBottomSheet(
    viewModel: GroupDetailViewModel = koinViewModel()
) {
    val sheetState = rememberModalBottomSheetState()

    val state = viewModel.state.collectAsStateWithLifecycle()

    ModalBottomSheet(
        onDismissRequest = {
            viewModel.onAction(GroupDetailAction.OnMemberBottomSheetToggle)
        },
        sheetState = sheetState
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            itemsIndexed(
                items = state.value.group.members
            ) { index, item ->
                if (index != 0) Spacer(modifier = Modifier.height(2.dp))

                UserListCard(
                    order = CardOrder.getOrderFrom(index, state.value.group.members.size),
                    user = item
                )
            }
        }
    }
}