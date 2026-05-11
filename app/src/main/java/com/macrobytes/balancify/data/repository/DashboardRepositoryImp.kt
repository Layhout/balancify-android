package com.macrobytes.balancify.data.repository

import com.macrobytes.balancify.data.data_source.dashboard.DashboardRemoteDataSource
import com.macrobytes.balancify.domain.model.DashboardModel
import com.macrobytes.balancify.domain.repository.DashboardRepository

class DashboardRepositoryImp(
    private val remoteDataSource: DashboardRemoteDataSource
) : DashboardRepository {
    override suspend fun getData(): Result<DashboardModel> {
        return Result.runCatching { remoteDataSource.getData() }
    }
}