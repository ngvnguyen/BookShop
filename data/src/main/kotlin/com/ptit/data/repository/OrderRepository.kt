package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.order.AllOrderData
import com.ptit.data.model.order.CreateOrderRequest
import com.ptit.data.model.order.OrderData
import com.ptit.data.model.order.UpdateOrderInfoRequest
import com.ptit.data.model.order.UpdateOrderStatusRequest

interface OrderRepository {
    suspend fun createOrder(token:String,createOrderRequest: CreateOrderRequest): RequestState<String>
    suspend fun updateOrderInfo(token:String,orderId: Int,updateOrderInfoRequest: UpdateOrderInfoRequest): RequestState<String>
    suspend fun updateOrderStatus(token:String,orderId:Int,orderStatusRequest: UpdateOrderStatusRequest): RequestState<String>
    suspend fun getOrderById(token:String,orderId:Int): RequestState<OrderData>
    suspend fun getOrders(token:String): RequestState<AllOrderData>
    suspend fun getOrdersAdmin(token:String): RequestState<AllOrderData>

}