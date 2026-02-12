package com.example.balancify.presentation.group_form

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.balancify.component.AppBar
import com.example.balancify.core.constant.BORDER_RADIUS_MD
import com.example.balancify.core.util.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupFormScreen(
    viewModel: GroupFormViewModel = koinViewModel(),
    onNavigateToSearchFriend: () -> Unit,
    onBackClick: () -> Unit
) {
    val localFocusManager = LocalFocusManager.current
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is GroupFormEvent.OnError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }

            is GroupFormEvent.OnAddMemberClicked -> {
                onNavigateToSearchFriend()
            }
        }
    }

    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Scaffold(
            topBar = {
                AppBar("Create a Group", onBackClick)
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            localFocusManager.clearFocus()
                        })
                    }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    item {
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text("Name *") },
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(BORDER_RADIUS_MD)
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item {
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            label = { Text("Description") },
                            shape = RoundedCornerShape(BORDER_RADIUS_MD),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    item {
                        Text(
                            "Members",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
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
                }
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create")
                }
            }
        }
    }
}