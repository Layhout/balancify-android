package com.example.balancify.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.DataUsage
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.balancify.component.AppBar
import com.example.balancify.core.constant.AppScreen
import com.example.balancify.presentation.home.component.FabMenu
import com.example.balancify.presentation.home.component.account.AccountScreen
import com.example.balancify.presentation.home.component.dashboard.DashboardScreen
import com.example.balancify.presentation.home.component.expense.ExpenseScreen
import com.example.balancify.presentation.home.component.group.GroupScreen

enum class NavDestination(
    val screen: AppScreen,
    val label: String,
    val icon: ImageVector
) {
    DASHBOARD(AppScreen.DASHBOARD, "Dashboard", Icons.Outlined.Dashboard),
    EXPENSES(AppScreen.EXPENSE, "Expenses", Icons.Outlined.DataUsage),
    GROUPS(AppScreen.GROUP, "Groups", Icons.Outlined.Groups),
    ACCOUNT(AppScreen.ACCOUNT, "Account", Icons.Outlined.AccountCircle)
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogoutComplete: () -> Unit,
    onNavigateToFriend: () -> Unit,
    onNavigateToGroupFrom: () -> Unit,
    onNavigateToGroupDetail: (String) -> Unit,
    onGroupListShouldRefreshFound: () -> Boolean?,
) {
    val navController = rememberNavController()
    val startDestination = NavDestination.DASHBOARD
    var selectedRoute by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
    var prevSelectedRoute by rememberSaveable { mutableStateOf(startDestination) }

    val shouldRefreshGroupList = onGroupListShouldRefreshFound()

    LaunchedEffect(shouldRefreshGroupList) {
        shouldRefreshGroupList?.let {
            if (it)
                onTabClick(
                    index = NavDestination.GROUPS.ordinal,
                    destination = NavDestination.GROUPS,
                    selectedRoute = selectedRoute,
                    prevSelectedRoute = prevSelectedRoute,
                    navController = navController,
                    onNavigate = { newIndex, newDest ->
                        selectedRoute = newIndex
                        prevSelectedRoute = newDest
                    },
                )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar(title = NavDestination.entries[selectedRoute].label) },
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                NavDestination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = index == selectedRoute,
                        label = { Text(destination.label, fontWeight = FontWeight.SemiBold) },
                        onClick = {
                            onTabClick(
                                index = index,
                                destination = destination,
                                selectedRoute = selectedRoute,
                                prevSelectedRoute = prevSelectedRoute,
                                navController = navController,
                                onNavigate = { newIndex, newDest ->
                                    selectedRoute = newIndex
                                    prevSelectedRoute = newDest
                                }
                            )
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
            FabMenu(
                onCreateGroupClick = onNavigateToGroupFrom
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            NavHost(
                navController,
                startDestination = startDestination.screen.route,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        initialOffset = { it / 15 },
                    ) + fadeIn()
                },
            ) {
                NavDestination.entries.forEach { destination ->
                    composable(destination.screen.route) {
                        when (destination) {
                            NavDestination.DASHBOARD -> DashboardScreen()
                            NavDestination.EXPENSES -> ExpenseScreen()
                            NavDestination.GROUPS -> GroupScreen(
                                onGroupListShouldRefreshFound = onGroupListShouldRefreshFound,
                                onNavigateToGroupDetail = onNavigateToGroupDetail
                            )

                            NavDestination.ACCOUNT -> AccountScreen(
                                onLogoutComplete = onLogoutComplete,
                                onNavigateToFriend = onNavigateToFriend,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun onTabClick(
    index: Int,
    destination: NavDestination,
    selectedRoute: Int,
    prevSelectedRoute: NavDestination,
    navController: NavHostController,
    onNavigate: (Int, NavDestination) -> Unit
) {
    if (selectedRoute != index) {
        navController.navigateToTab(
            route = destination.screen.route,
            popUpToRoute = prevSelectedRoute.screen.route
        )
        onNavigate(index, destination)
    }
}

private fun NavHostController.navigateToTab(route: String, popUpToRoute: String) {
    this.navigate(route) {
        popUpTo(popUpToRoute) {
            saveState = true
            inclusive = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
