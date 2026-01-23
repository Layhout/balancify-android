package com.example.balancify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.balancify.navigatin.NavigationRoot
import com.example.balancify.ui.theme.BalancifyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BalancifyTheme {
                val navController = rememberNavController()
                NavigationRoot(navController = navController)
            }
        }
    }
}