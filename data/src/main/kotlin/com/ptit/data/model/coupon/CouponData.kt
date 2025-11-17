package com.ptit.data.model.coupon


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CouponData(
    @SerialName("code")
    val code: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("description")
    val description: String,
    @SerialName("discount_type")
    val discountType: CouponType,
    @SerialName("discount_value")
    val discountValue: Int,
    @SerialName("expires_at")
    val expiresAt: String,
    @SerialName("id")
    val id: String,
    @SerialName("maximum_discount_amount")
    val maximumDiscountAmount: Int,
    @SerialName("minimum_order_amount")
    val minimumOrderAmount: Int,
    @SerialName("name")
    val name: String,
    @SerialName("starts_at")
    val startsAt: String,
    @SerialName("status")
    val status: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("usage_limit")
    val usageLimit: Int,
    @SerialName("usage_limit_per_customer")
    val usageLimitPerCustomer: Int
){
    @Serializable
    enum class CouponType(val title:String){
        PERCENTAGE("Percentage"),
        FIXED_AMOUNT("Fixed Amount"),
        FREE_SHIPPING("Free Shipping")
    }
}