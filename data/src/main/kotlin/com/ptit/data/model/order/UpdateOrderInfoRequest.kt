package com.ptit.data.model.order


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrderInfoRequest(
    @SerialName("city")
    val city: String,
    @SerialName("district")
    val district: String,
    @SerialName("receiver_name")
    val receiverName: String,
    @SerialName("receiver_phone")
    val receiverPhone: String,
    @SerialName("street")
    val street: String,
    @SerialName("ward")
    val ward: String
)