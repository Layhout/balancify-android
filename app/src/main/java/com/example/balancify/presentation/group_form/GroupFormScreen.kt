package com.example.balancify.presentation.group_form

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.component.AppBar
import com.example.balancify.component.CardOrder
import com.example.balancify.core.constant.SearchResult
import com.example.balancify.core.util.ObserveAsEvents
import com.example.balancify.presentation.group_form.component.AddMemberButton
import com.example.balancify.presentation.group_form.component.FormInputs
import com.example.balancify.presentation.group_form.component.MemberCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupFormScreen(
    viewModel: GroupFormViewModel = koinViewModel(),
    onSearchResultFound: () -> SearchResult.Friend?,
    onNavigateToSearchFriend: () -> Unit,
    onCreateSuccess: () -> Unit,
    onEditSuccess: () -> Unit,
    onBackClick: () -> Unit,
) {
    val localFocusManager = LocalFocusManager.current
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    val searchResult = onSearchResultFound()

    LaunchedEffect(searchResult?.data) {
        searchResult?.data?.let {
            viewModel.onAction(GroupFormAction.OnAddMember(it))
        }
    }

    ObserveAsEvents(viewModel.events) {
        when (it) {
            is GroupFormEvent.OnError -> {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }

            is GroupFormEvent.OnAddMemberClicked -> {
                onNavigateToSearchFriend()
            }

            is GroupFormEvent.OnSaveSuccess -> {
                if (state.value.isEditing)
                    onEditSuccess()
                else
                    onCreateSuccess()
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
                        FormInputs()
                    }
                    item {
                        AddMemberButton()
                    }
                    itemsIndexed(
                        items = state.value.members,
                    ) { index, item ->
                        if (index != 0) Spacer(modifier = Modifier.height(2.dp))
                        MemberCard(
                            item = item,
                            order = CardOrder.getOrderFrom(index, state.value.members.size),
                        )
                    }
                }
                Button(
                    onClick = {
                        viewModel.onAction(GroupFormAction.OnSaveClick)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.value.isLoading,
                ) {
                    if (state.value.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    if (state.value.isEditing)
                        Text("Update")
                    else
                        Text("Create")
                }
            }
        }
    }
}