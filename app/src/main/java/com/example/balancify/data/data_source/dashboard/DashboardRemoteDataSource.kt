package com.example.balancify.data.data_source.dashboard

import com.example.balancify.domain.model.DashboardModel

interface DashboardRemoteDataSource {
    suspend fun getData(): DashboardModel
}