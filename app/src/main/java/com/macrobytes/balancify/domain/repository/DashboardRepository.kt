package com.macrobytes.balancify.domain.repository

import com.macrobytes.balancify.domain.model.DashboardModel

interface DashboardRepository {
    suspend fun getData(): Result<DashboardModel>
}