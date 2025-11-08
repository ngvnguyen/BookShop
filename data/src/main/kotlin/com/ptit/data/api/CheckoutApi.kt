package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.checkout.CheckoutData
import com.ptit.data.model.checkout.CheckoutRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CheckoutApi {
    @POST("/api/v1/checkout")
    suspend fun checkout(
        @Header("Authorization") token:String,
        @Body body: CheckoutRequest
    ): Response<ResponseEntity<CheckoutData>>
}