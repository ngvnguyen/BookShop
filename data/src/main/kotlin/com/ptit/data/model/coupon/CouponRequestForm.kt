package com.ptit.data.model.coupon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CouponRequestForm(
    @SerialName("code")
    val code: String,
    @SerialName("description")
    val description: String,
    @SerialName("discount_type")
    val discountType: String,
    @SerialName("discount_value")
    val discountValue: Int,
    @SerialName("expires_at")
    val expiresAt: String,
    @SerialName("maximum_discount_amount")
    val maximumDiscountAmount: Int,
    @SerialName("minimum_order_amount")
    val minimumOrderAmount: Int,
    @SerialName("name")
    val name: String,
    @SerialName("starts_at")
    val startsAt: String,
    @SerialName("usage_limit")
    val usageLimit: Int,
    @SerialName("usage_limit_per_customer")
    val usageLimitPerCustomer: Int,
    @SerialName("status")
    val status: CouponData.Status
)