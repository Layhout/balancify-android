package com.example.balancify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            BalancifyTheme {
//                HomeScreen()
//            }
            val viewModel: MainViewModel by viewModels()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    MainUiState.Loading -> CircularProgressIndicator()
                    MainUiState.SignedOut -> SignInOrUpView()
                    MainUiState.SignedIn -> Button(onClick = { viewModel.signOut() }) { Text("Sign out") }
                }
            }
        }
    }
}