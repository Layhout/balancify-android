package com.macrobytes.balancify

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.macrobytes.balancify.navigatin.NavigationRoot
import com.macrobytes.balancify.ui.theme.BalancifyTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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