package com.example.balancify.presentation.home

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.balancify.core.AppScreen
import com.example.balancify.presentation.home.component.account.AccountScreen
import com.example.balancify.presentation.home.component.dashboard.DashboardScreen
import com.example.balancify.presentation.home.component.expense.ExpenseScreen
import com.example.balancify.presentation.home.component.group.GroupScreen

enum class NavDestination(
    val screen: AppScreen,
    val label: String,
    val icon: ImageVector
) {
    DASHBOARD(AppScreen.DASHBOARD, "Dashboard", Icons.Rounded.Dashboard),
    EXPENSES(AppScreen.EXPENSE, "Expenses", Icons.Rounded.PieChart),
    GROUPS(AppScreen.GROUP, "Groups", Icons.Rounded.People),
    ACCOUNT(AppScreen.ACCOUNT, "Account", Icons.Rounded.AccountCircle)
}


@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val startDestination = NavDestination.DASHBOARD
    var selectedRoute by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                NavDestination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = index == selectedRoute,
                        label = { Text(destination.label) },
                        onClick = {
                            navController.navigate(route = destination.screen.route)
                            selectedRoute = index
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.label
                            )
                        })
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(
                    Icons.Rounded.Add,
                    "New Expense"
                )
            }
        }) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            NavHost(
                navController,
                startDestination = startDestination.screen.route
            ) {
                NavDestination.entries.forEach { destination ->
                    composable(destination.screen.route) {
                        when (destination) {
                            NavDestination.DASHBOARD -> DashboardScreen()
                            NavDestination.EXPENSES -> ExpenseScreen()
                            NavDestination.GROUPS -> GroupScreen()
                            NavDestination.ACCOUNT -> AccountScreen()
                        }
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}