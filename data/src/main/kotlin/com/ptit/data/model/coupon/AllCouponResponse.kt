package com.ptit.data.model.coupon


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllCouponResponse(
    @SerialName("coupons")
    val coupons: List<CouponData>,
    @SerialName("page")
    val page: Int,
    @SerialName("page_size")
    val pageSize: Int,
    @SerialName("pages")
    val pages: Int,
    @SerialName("total")
    val total: Int
)