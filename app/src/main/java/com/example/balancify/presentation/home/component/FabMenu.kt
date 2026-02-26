package com.example.balancify.presentation.home.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DataSaverOn
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.presentation.home.HomeAction
import com.example.balancify.presentation.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FabMenu(
    viewModel: HomeViewModel = koinViewModel(),
    onCreateGroupClick: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    FloatingActionButtonMenu(
        expanded = state.value.toggleFab,
        button = {
            ToggleFloatingActionButton(
                checked = state.value.toggleFab,
                onCheckedChange = {
                    viewModel.onAction(HomeAction.OnToggleFabClick)
                }
            ) {
                Icon(
                    if (state.value.toggleFab)
                        Icons.Outlined.Close
                    else Icons.Outlined.Add,
                    contentDescription = null
                )
            }
        }
    ) {
        FloatingActionButtonMenuItem(
            onClick = {},
            text = { Text("Expense") },
            icon = { Icon(Icons.Outlined.DataSaverOn, contentDescription = null) },
        )
        FloatingActionButtonMenuItem(
            onClick = {
                onCreateGroupClick()
                viewModel.onAction(HomeAction.OnToggleFabClick)
            },
            text = { Text("Group") },
            icon = { Icon(Icons.Outlined.GroupAdd, contentDescription = null) },
        )
    }
}