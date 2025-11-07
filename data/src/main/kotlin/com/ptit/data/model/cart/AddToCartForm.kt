package com.ptit.data.model.cart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToCartForm(
    @SerialName("product_id") val productId:Int,
    @SerialName("quantity") val quantity:Int
)
