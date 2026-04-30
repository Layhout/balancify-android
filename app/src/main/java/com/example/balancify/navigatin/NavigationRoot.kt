package com.example.balancify.navigatin

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.balancify.core.constant.SearchType
import com.example.balancify.presentation.expense_detail.ExpenseDetailScreen
import com.example.balancify.presentation.expense_form.ExpenseFormScreen
import com.example.balancify.presentation.friend.FriendScreen
import com.example.balancify.presentation.group_detail.GroupDetailScreen
import com.example.balancify.presentation.group_form.GroupFormScreen
import com.example.balancify.presentation.home.HomeScreen
import com.example.balancify.presentation.login.LoginScreen
import com.example.balancify.presentation.notification.NotificationScreen
import com.example.balancify.presentation.search.SearchScreen
import com.example.balancify.service.AuthService
import org.koin.compose.koinInject

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    val authService: AuthService = koinInject()

    NavHost(
        navController = navController,
        startDestination = if (authService.isLoggedIn) Routes.Home else Routes.Login,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
        },
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
                    navController.navigate(Routes.GroupFrom())
                },
                onNavigateToGroupDetail = {
                    navController.navigate(Routes.GroupDetail(it))
                },
                onNavigateToExpenseForm = {
                    navController.navigate(Routes.ExpenseForm())
                },
                onNavigateToExpenseDetail = {
                    navController.navigate(Routes.ExpenseDetail(it))
                },
                onNavigateToNotification = {
                    navController.navigate(Routes.Notification)
                },
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
                },
            ) {
                navController.popBackStack()
            }
        }
        composable<Routes.Search> {
            SearchScreen {
                navController.popBackStack()
            }
        }
        composable<Routes.GroupDetail> {
            GroupDetailScreen(
                onNavigateToGroupFrom = {
                    navController.navigate(Routes.GroupFrom(it))
                },
                onNavigateToExpenseDetail = {
                    navController.navigate(Routes.ExpenseDetail(it))
                },
                onNavigateToExpenseForm = {
                    navController.navigate(Routes.ExpenseForm())
                }
            ) {
                navController.popBackStack()
            }
        }
        composable<Routes.ExpenseDetail> {
            ExpenseDetailScreen(
                onNavigateToExpenseFrom = {
                    navController.navigate(Routes.ExpenseForm(it))
                }
            ) {
                navController.popBackStack()
            }
        }
        composable<Routes.ExpenseForm> {
            ExpenseFormScreen(
                onNavigateToSearch = {
                    navController.navigate(
                        Routes.Search(
                            type = it
                        )
                    )
                },
            ) {
                navController.popBackStack()
            }
        }
        composable<Routes.Notification> {
            NotificationScreen(
                onNavigateToFriend = {
                    navController.navigate(Routes.Friend)
                },
                onNavigateToExpenseDetail = {
                    navController.navigate(Routes.ExpenseDetail(it))
                },
                onNavigateToGroupDetail = {
                    navController.navigate(Routes.GroupDetail(it))
                },
            ) {
                navController.popBackStack()
            }
        }
    }
}