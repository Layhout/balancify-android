package com.example.balancify.presentation.group_form.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.presentation.group_form.GroupFormAction
import com.example.balancify.presentation.group_form.GroupFormViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddMemberButton(
    viewModel: GroupFormViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Members (${state.value.members.size})",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = if (state.value.isMemberInvalid)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onBackground
                )
            )
            TextButton(
                onClick = {
                    viewModel.onAction(GroupFormAction.OnAddMemberClick)
                }
            ) {
                Icon((Icons.Outlined.PersonAdd), contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Members")
            }
        }
        if (state.value.isMemberInvalid)
            Text(
                "Member cannot be greater than 10",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
    }
}