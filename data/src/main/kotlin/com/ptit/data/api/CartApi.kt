package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.cart.AddToCartForm
import com.ptit.data.model.cart.CartData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CartApi {
    @POST("/api/v1/carts/items")
    suspend fun addItemToCart(
        @Header("Authorization") token:String,
        @Body body: AddToCartForm
    ): Response<ResponseEntity<Unit>>

    @PATCH("/api/v1/carts/items/{id}")
    suspend fun updateItemCart(
        @Header("Authorization") token:String,
        @Path("id") id:Int,
        @Query("quantity") quantity:Int
    ): Response<ResponseEntity<CartData>>

    @GET("/api/v1/carts")
    suspend fun getCart(
        @Header("Authorization") token:String
    ): Response<ResponseEntity<CartData>>

    @DELETE("/api/v1/carts/items/{id}")
    suspend fun deleteCartItem(
        @Header("Authorization") token:String,
        @Path("id") id:Int
    ): Response<Unit>
}