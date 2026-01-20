package com.example.balancify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.balancify.presentation.home.HomeScreen
import com.example.balancify.ui.theme.BalancifyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BalancifyTheme {
                HomeScreen()
            }
        }
    }
}