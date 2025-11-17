package com.ptit.data.model.dashboard


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderStatusCountData(
    @SerialName("count")
    val count: Int,
    @SerialName("order_status")
    val orderStatus: String
)