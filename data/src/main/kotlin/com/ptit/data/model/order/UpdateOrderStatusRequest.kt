package com.ptit.data.model.order

import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrderStatusRequest(
    val status: OrderStatus
){
    @Serializable
    enum class OrderStatus{
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }
}
