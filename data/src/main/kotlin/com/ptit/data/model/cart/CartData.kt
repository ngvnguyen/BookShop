package com.ptit.data.model.cart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartData(
    @SerialName("cart_items")
    val cartItems: List<CartItem>,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("id")
    val id: Int,
    @SerialName("summary")
    val summary: Summary,
    @SerialName("updated_at")
    val updatedAt: String
) {
    @Serializable
    data class CartItem(
        @SerialName("discount")
        val discount: Double,
        @SerialName("final_price")
        val finalPrice: Double,
        @SerialName("id")
        val id: Int,
        @SerialName("image_url")
        val imageUrl: String?,
        @SerialName("product_id")
        val productId: Int,
        @SerialName("product_name")
        val productName: String,
        @SerialName("product_status")
        val productStatus: String,
        @SerialName("quantity")
        val quantity: Int,
        @SerialName("unit_price")
        val unitPrice: Double
    )

    @Serializable
    data class Summary(
        @SerialName("subtotal")
        val subtotal: Double,
        @SerialName("total_quantity")
        val totalQuantity: Int
    )
}