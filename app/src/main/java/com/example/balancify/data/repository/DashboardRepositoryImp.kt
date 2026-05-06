package com.example.balancify.data.repository

import com.example.balancify.data.data_source.dashboard.DashboardRemoteDataSource
import com.example.balancify.domain.model.DashboardModel
import com.example.balancify.domain.repository.DashboardRepository

class DashboardRepositoryImp(
    private val remoteDataSource: DashboardRemoteDataSource
) : DashboardRepository {
    override suspend fun getData(): Result<DashboardModel> {
        return Result.runCatching { remoteDataSource.getData() }
    }
}