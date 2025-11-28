package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.cart.AddToCartForm
import com.ptit.data.model.cart.CartData

interface CartRepository {
    suspend fun getCart(accessToken:String): RequestState<CartData>
    suspend fun updateCartItem(
        accessToken: String,
        productId:Int,
        quantity:Int
    ): RequestState<CartData>
    suspend fun addItemToCart(
        accessToken: String,
        addToCartForm: AddToCartForm
    ): RequestState<Int>
    suspend fun deleteCartItem(
        accessToken: String,
        productId:Int
    ): RequestState<Unit>
}