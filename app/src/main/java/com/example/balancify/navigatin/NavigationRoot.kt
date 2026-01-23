package com.example.balancify.navigatin

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.balancify.presentation.home.HomeScreen
import com.example.balancify.presentation.login.LoginScreen
import com.example.balancify.service.AuthService
import org.koin.compose.koinInject

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    val authService: AuthService = koinInject()

    NavHost(
        navController = navController,
        startDestination = if (authService.isLoggedIn) Routes.Home else Routes.Intro,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
    ) {
        composable<Routes.Intro> {
            LoginScreen(
                onLoginComplete = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Intro) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable<Routes.Home> {
            HomeScreen()
        }
    }
}