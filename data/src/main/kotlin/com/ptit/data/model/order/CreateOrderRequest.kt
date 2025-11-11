package com.ptit.data.model.order


import com.ptit.data.model.checkout.CheckoutData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    @SerialName("cart_items")
    val cartItems: List<CartItem>,
    @SerialName("coupon_code")
    val couponCode: String?,
    @SerialName("note")
    val note: String
) {
    @Serializable
    data class CartItem(
        @SerialName("id")
        val id: Int
    )
}

