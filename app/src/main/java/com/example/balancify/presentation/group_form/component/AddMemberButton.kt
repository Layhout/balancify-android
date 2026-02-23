package com.example.balancify.presentation.group_form.component

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.balancify.presentation.group_form.GroupFormAction
import com.example.balancify.presentation.group_form.GroupFormViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddMemberButton(
    viewModel: GroupFormViewModel = koinViewModel()
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Members",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold
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
}