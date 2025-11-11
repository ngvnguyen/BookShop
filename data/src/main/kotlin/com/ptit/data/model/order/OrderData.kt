package com.ptit.data.model.order


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderData(
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("id")
    val id: Int,
    @SerialName("items")
    val items: List<Item>,
    @SerialName("payment_method")
    val paymentMethod: String,
    @SerialName("shipping_info")
    val shippingInfo: ShippingInfo,
    @SerialName("status")
    val status: String,
    @SerialName("summary")
    val summary: Summary,
    @SerialName("update_at")
    val updateAt: String?
) {
    @Serializable
    data class Item(
        @SerialName("discount")
        val discount: Int,
        @SerialName("discounted_price")
        val discountedPrice: Int,
        @SerialName("id")
        val id: Int,
        @SerialName("image_url")
        val imageUrl: String,
        @SerialName("product_id")
        val productId: Int,
        @SerialName("product_name")
        val productName: String,
        @SerialName("quantity")
        val quantity: Int,
        @SerialName("total_price")
        val totalPrice: Int,
        @SerialName("unit_price")
        val unitPrice: Int
    )

    @Serializable
    data class ShippingInfo(
        @SerialName("receiver_address")
        val receiverAddress: String,
        @SerialName("receiver_name")
        val receiverName: String,
        @SerialName("receiver_phone")
        val receiverPhone: String
    )

    @Serializable
    data class Summary(
        @SerialName("discount_fee")
        val discountFee: Int,
        @SerialName("final_price")
        val finalPrice: Int,
        @SerialName("shipping_fee")
        val shippingFee: Int,
        @SerialName("subtotal")
        val subtotal: Int
    )
}