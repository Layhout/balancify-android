package com.example.balancify.presentation.group_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DataSaverOn
import androidx.compose.material.icons.outlined.PeopleAlt
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.presentation.group_detail.GroupDetailAction
import com.example.balancify.presentation.group_detail.GroupDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GroupDetailFooter(
    viewModel: GroupDetailViewModel = koinViewModel(),
    onNavigateToExpenseForm: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
//    val globalAppStateManager: GlobalAppStateManager = koinInject()

    Row(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
//                globalAppStateManager.setSearchResult(SearchResult.Group(state.value.group))
                onNavigateToExpenseForm()
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Outlined.DataSaverOn, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Add Expense")
        }
        FilledIconButton(
            enabled = state.value.enableAllAction,
            onClick = {
                viewModel.onAction(GroupDetailAction.OnMemberBottomSheetToggle)
            },
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
        ) {
            Icon(Icons.Outlined.PeopleAlt, contentDescription = null)
        }
    }
}