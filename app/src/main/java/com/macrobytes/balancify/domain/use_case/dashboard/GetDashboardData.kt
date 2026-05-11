package com.macrobytes.balancify.domain.use_case.dashboard

import com.macrobytes.balancify.domain.repository.DashboardRepository

class GetDashboardData(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke() = dashboardRepository.getData()
}