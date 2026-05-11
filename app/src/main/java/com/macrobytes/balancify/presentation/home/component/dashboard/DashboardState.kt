package com.macrobytes.balancify.presentation.home.component.dashboard

import com.macrobytes.balancify.domain.model.DashboardModel
import com.macrobytes.balancify.domain.model.UserModel

data class DashboardState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val data: DashboardModel? = null,
    val localUser: UserModel? = null,
)