package com.example.balancify.presentation.home.component.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(accountViewModel: AccountViewModel = viewModel()) {
    val state = accountViewModel.state.collectAsStateWithLifecycle()
    val isLoggedIn = state.value.isLoggedIn
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Account Screen")
            Text("Is Logged In: $isLoggedIn")
            if (!isLoggedIn) {
                Button(onClick = {
                    coroutineScope.launch {
                        accountViewModel.onLoginClick(context)
                    }
                }) {
                    Text(
                        "Login"
                    )
                }
            } else {
                Button(onClick = { }) { Text("Logout") }
            }
        }
    }
}