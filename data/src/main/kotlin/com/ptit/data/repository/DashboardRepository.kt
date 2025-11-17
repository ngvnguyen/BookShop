package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.dashboard.OrderStatusCountData
import com.ptit.data.model.dashboard.OverviewData
import com.ptit.data.model.dashboard.RevenueByMonthData

interface DashboardRepository {
    suspend fun getOrderStatusCount(accessToken:String): RequestState<List<OrderStatusCountData>>
    suspend fun getRevenueByMonth(accessToken:String): RequestState<List<RevenueByMonthData>>
    suspend fun getOverviewData(accessToken:String): RequestState<OverviewData>
}