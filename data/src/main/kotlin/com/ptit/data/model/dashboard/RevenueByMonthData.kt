package com.ptit.data.model.dashboard


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RevenueByMonthData(
    @SerialName("month")
    val month: Int,
    @SerialName("total_revenue")
    val totalRevenue: Int,
    @SerialName("year")
    val year: Int
)