package com.ptit.data.model.checkout


import com.ptit.data.model.cart.CartData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutData(
    @SerialName("coupon_info")
    val couponInfo: CouponInfo?,
    @SerialName("items")
    val items: List<Item>,
    @SerialName("payment_methods")
    val paymentMethods: String,
    @SerialName("shipping_address")
    val shippingAddress: ShippingAddress,
    @SerialName("summary")
    val summary: Summary
) {
    @Serializable
    data class CouponInfo(
        @SerialName("code")
        val code: String,
        @SerialName("discount_type")
        val discountType: String,
        @SerialName("discount_value")
        val discountValue: Int,
        @SerialName("expired_at")
        val expiredAt: String
    )

    @Serializable
    data class Item(
        @SerialName("final_price")
        val finalPrice: Int,
        @SerialName("image_url")
        val imageUrl: String,
        @SerialName("product_id")
        val productId: Int,
        @SerialName("product_name")
        val productName: String,
        @SerialName("quantity")
        val quantity: Int,
        @SerialName("unit_price")
        val unitPrice: Int
    )

    @Serializable
    data class ShippingAddress(
        @SerialName("address")
        val address: String,
        @SerialName("name")
        val name: String,
        @SerialName("phone")
        val phone: String
    )

    @Serializable
    data class Summary(
        @SerialName("cart_discount")
        val cartDiscount: Int,
        @SerialName("grand_total")
        val grandTotal: Int,
        @SerialName("shipping_fee")
        val shippingFee: Int,
        @SerialName("subtotal")
        val subtotal: Int,
        @SerialName("total_quantity")
        val totalQuantity: Int
    )
}