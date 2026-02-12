package com.example.balancify.navigatin

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.balancify.core.constant.SearchType
import com.example.balancify.presentation.friend.FriendScreen
import com.example.balancify.presentation.group_form.GroupFormScreen
import com.example.balancify.presentation.home.HomeScreen
import com.example.balancify.presentation.login.LoginScreen
import com.example.balancify.presentation.search.SearchScreen
import com.example.balancify.service.AuthService
import org.koin.compose.koinInject

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    val authService: AuthService = koinInject()

    NavHost(
        navController = navController,
        startDestination = if (authService.isLoggedIn) Routes.Home else Routes.Login,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
    ) {
        composable<Routes.Login> {
            LoginScreen {
                navController.navigate(Routes.Home) {
                    popUpTo(Routes.Login) {
                        inclusive = true
                    }
                }
            }
        }
        composable<Routes.Home> {
            HomeScreen(
                onLogoutComplete = {
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Home) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToFriend = {
                    navController.navigate(Routes.Friend)
                },
                onNavigateToGroupFrom = {
                    navController.navigate(Routes.GroupFrom)
                }
            )
        }
        composable<Routes.Friend> {
            FriendScreen {
                navController.popBackStack()
            }
        }
        composable<Routes.GroupFrom> {
            GroupFormScreen(
                onNavigateToSearchFriend = {
                    navController.navigate(
                        Routes.Search(
                            type = SearchType.FRIEND
                        )
                    )
                }
            ) {
                navController.popBackStack()
            }
        }
        composable<Routes.Search> {
            val arg = it.toRoute<Routes.Search>()

            SearchScreen(
                type = arg.type,
            ) {
                navController.popBackStack()
            }
        }
    }
}