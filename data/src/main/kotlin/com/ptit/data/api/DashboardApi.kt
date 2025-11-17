package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.dashboard.OrderStatusCountData
import com.ptit.data.model.dashboard.OverviewData
import com.ptit.data.model.dashboard.RevenueByMonthData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface DashboardApi {
    @GET("api/v1/admin/dashboard/statistics/orders/status")
    suspend fun getOrderStatusCount(
        @Header("Authorization") token:String
    ): Response<ResponseEntity<List<OrderStatusCountData>>>

    @GET("api/v1/admin/dashboard/statistics/revenue-by-month")
    suspend fun getRevenueByMonth(
        @Header("Authorization") token:String
    ): Response<ResponseEntity<List<RevenueByMonthData>>>

    @GET("api/v1/admin/dashboard/overview")
    suspend fun getOverviewData(
        @Header("Authorization") token:String
    ): Response<ResponseEntity<OverviewData>>
}