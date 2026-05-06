package com.example.balancify.domain.repository

import com.example.balancify.domain.model.DashboardModel

interface DashboardRepository {
    suspend fun getData(): Result<DashboardModel>
}