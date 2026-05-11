package com.macrobytes.balancify.data.data_source.dashboard

import com.macrobytes.balancify.domain.model.DashboardModel

interface DashboardRemoteDataSource {
    suspend fun getData(): DashboardModel
}