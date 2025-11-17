package com.ptit.data.model.dashboard


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OverviewData(
    @SerialName("inactive_users")
    val inactiveUsers: Int,
    @SerialName("out_of_stock_books")
    val outOfStockBooks: Int,
    @SerialName("total_books")
    val totalBooks: Int,
    @SerialName("total_orders")
    val totalOrders: Int,
    @SerialName("total_revenue")
    val totalRevenue: Int?,
    @SerialName("total_users")
    val totalUsers: Int
)