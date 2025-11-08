package com.ptit.data.model.checkout


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutRequest(
    @SerialName("cart_items")
    val cartItems: List<CartItem>,
    @SerialName("coupon_code")
    val couponCode: String?
) {
    @Serializable
    data class CartItem(
        @SerialName("id")
        val id: Int
    )
}