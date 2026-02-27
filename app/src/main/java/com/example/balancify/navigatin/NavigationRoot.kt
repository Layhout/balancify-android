package com.example.balancify.navigatin

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.balancify.core.constant.SavedStateKey
import com.example.balancify.core.constant.SearchResult
import com.example.balancify.core.constant.SearchType
import com.example.balancify.presentation.friend.FriendScreen
import com.example.balancify.presentation.group_detail.GroupDetailScreen
import com.example.balancify.presentation.group_form.GroupFormScreen
import com.example.balancify.presentation.home.HomeScreen
import com.example.balancify.presentation.login.LoginScreen
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
        composable<Routes.Home> { entry ->
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
                },
                onNavigateToGroupDetail = {
                    navController.navigate(Routes.GroupDetail(it))
                },
                onGroupListShouldRefreshFound = {
                    entry.savedStateHandle.remove<Boolean>(
                        SavedStateKey.GROUP_LIST_SHOULD_REFRESH.value
                    )
                },
            )
        }
        composable<Routes.Friend> {
            FriendScreen {
                navController.popBackStack()
            }
        }
        composable<Routes.GroupFrom> { entry ->
            GroupFormScreen(
                onNavigateToSearchFriend = {
                    navController.navigate(
                        Routes.Search(
                            type = SearchType.FRIEND
                        )
                    )
                },
                onCreateSuccess = {
                    navController.setPrevStackState(
                        SavedStateKey.GROUP_LIST_SHOULD_REFRESH.value,
                        true
                    )
                    navController.popBackStack()
                },
                onSearchResultFound = {
                    val searchResult = entry.savedStateHandle.remove<SearchResult>(
                        SavedStateKey.FRIEND_SEARCH_RESULT.value
                    )

                    searchResult as? SearchResult.Friend
                },
                onEditSuccess = {
                    navController.setPrevStackState(
                        SavedStateKey.GROUP_DID_UPDATE.value,
                        true
                    )
                    navController.popBackStack()
                }
            ) {
                navController.popBackStack()
            }
        }
        composable<Routes.Search> {
            SearchScreen(
                onResultSelected = { searchResult ->
                    navController.setPrevStackState(
                        if (searchResult is SearchResult.Friend)
                            SavedStateKey.FRIEND_SEARCH_RESULT.value
                        else
                            SavedStateKey.GROUP_SEARCH_RESULT.value,
                        searchResult
                    )
                    navController.popBackStack()
                }
            ) {
                navController.popBackStack()
            }
        }
        composable<Routes.GroupDetail> {
            GroupDetailScreen(
                onLeaveGroupSuccess = {
                    navController.setPrevStackState(
                        SavedStateKey.GROUP_LIST_SHOULD_REFRESH.value,
                        true
                    )
                    navController.popBackStack()
                },
                onNavigateToGroupFrom = {
                    navController.navigate(Routes.GroupFrom(it))
                }
            ) {
                navController.popBackStack()
            }
        }
    }
}

private fun NavHostController.setPrevStackState(key: String, value: Any) {
    previousBackStackEntry?.savedStateHandle?.set(key, value)
}