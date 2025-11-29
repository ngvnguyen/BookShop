package com.ptit.data.model.order


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllOrderData(
    @SerialName("orders")
    val orders: List<OrderData>,
    @SerialName("page")
    val page: Int,
    @SerialName("page_size")
    val pageSize: Int,
    @SerialName("pages")
    val pages: Int,
    @SerialName("total")
    val total: Int?
)