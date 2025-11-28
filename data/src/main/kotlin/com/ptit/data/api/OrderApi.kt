package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.order.AllOrderData
import com.ptit.data.model.order.CreateOrderRequest
import com.ptit.data.model.order.OrderData
import com.ptit.data.model.order.UpdateOrderInfoRequest
import com.ptit.data.model.order.UpdateOrderStatusRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderApi {
    @POST("/api/v1/orders")
    suspend fun createOrder(
        @Header("Authorization") token:String,
        @Body body: CreateOrderRequest
    ): Response<ResponseEntity<OrderData>>

    @PUT("/api/v1/orders/{id}")
    suspend fun updateOrderInfo(
        @Header("Authorization") token:String,
        @Path("id") id: Int,
        @Body body: UpdateOrderInfoRequest
    ): Response<ResponseEntity<OrderData>>

    // Required admin role
    @PATCH("/api/v1/orders/{id}/status")
    suspend fun updateOrderStatus(
        @Header("Authorization") token:String,
        @Path("id") id: Int,
        @Body body : UpdateOrderStatusRequest
    ): Response<ResponseEntity<OrderData>>

    @GET("/api/v1/orders/{id}")
    suspend fun getOrderById(
        @Header("Authorization") token:String,
        @Path("id") id:Int
    ): Response<ResponseEntity<OrderData>>

    @GET("/api/v1/orders")
    suspend fun getOrders(
        @Header("Authorization") token:String
    ): Response<ResponseEntity<AllOrderData>>
}