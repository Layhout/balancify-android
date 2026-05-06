package com.example.balancify.domain.use_case.dashboard

import com.example.balancify.domain.repository.DashboardRepository

class GetDashboardData(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke() = dashboardRepository.getData()
}