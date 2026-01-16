package com.example.balancify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.People
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.balancify.ui.theme.BalancifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeScreen()
        }
    }
}


@Composable
fun HomeScreen() {
    BalancifyTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                    NavigationBarItem(selected = true, onClick = {}, icon = {
                        Icon(
                            Icons.Rounded.Dashboard,
                            contentDescription = "Dashboard"
                        )
                    }, label = { Text("Dashboard") })
                    NavigationBarItem(selected = false, onClick = {}, icon = {
                        Icon(
                            Icons.Rounded.PieChart,
                            contentDescription = "Expenses"
                        )
                    }, label = { Text("Expenses") })
                    NavigationBarItem(selected = false, onClick = {}, icon = {
                        Icon(
                            Icons.Rounded.People,
                            contentDescription = "Groups"
                        )
                    }, label = { Text("Groups") })
                    NavigationBarItem(selected = false, onClick = {}, icon = {
                        Icon(
                            Icons.Rounded.AccountCircle,
                            contentDescription = "Account"
                        )
                    }, label = { Text("Account") })
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { }) {
                    Icon(
                        Icons.Rounded.Add,
                        "New Expense"
                    )
                }
            }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HomeScreen()
}