package com.ptit.data.repository.impl

import android.view.PixelCopy
import androidx.compose.foundation.shape.RoundedCornerShape
import com.ptit.data.RequestState
import com.ptit.data.api.OrderApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.cart.CartData
import com.ptit.data.model.order.AllOrderData
import com.ptit.data.model.order.CreateOrderRequest
import com.ptit.data.model.order.OrderData
import com.ptit.data.model.order.UpdateOrderInfoRequest
import com.ptit.data.model.order.UpdateOrderStatusRequest
import com.ptit.data.repository.OrderRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class OrderRepositoryImpl(private val orderApi: OrderApi): OrderRepository {
    override suspend fun createOrder(
        token: String,
        createOrderRequest: CreateOrderRequest
    ): RequestState<String> {
        try{
            val response = orderApi.createOrder("Bearer $token",createOrderRequest)
            if (response.isSuccessful){
                val message = response.body()?.message?:"Order created successfully"
                return RequestState.SUCCESS(message)
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<OrderData>>(it).message
                return RequestState.ERROR(errMsg)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun updateOrderInfo(
        token: String,
        orderId: Int,
        updateOrderInfoRequest: UpdateOrderInfoRequest
    ): RequestState<String> {
        try{
            val response = orderApi.updateOrderInfo("Bearer $token",orderId,updateOrderInfoRequest)
            if (response.isSuccessful){
                val message = response.body()?.message?:"Information update successfully"
                return RequestState.SUCCESS(message)
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<OrderData>>(it).message
                return RequestState.ERROR(errMsg)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun updateOrderStatus(
        token: String,
        orderId: Int,
        orderStatusRequest: UpdateOrderStatusRequest
    ): RequestState<String> {
        try{
            val response = orderApi.updateOrderStatus("Bearer $token",orderId,orderStatusRequest)
            if (response.isSuccessful){
                val message = response.body()?.message?:"Status updated successfully"
                return RequestState.SUCCESS(message)
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<OrderData>>(it).message
                return RequestState.ERROR(errMsg)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun getOrderById(
        token: String,
        orderId: Int
    ): RequestState<OrderData> {
        try{
            val response = orderApi.getOrderById("Bearer $token",orderId)
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<OrderData>>(it).message
                return RequestState.ERROR(errMsg)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun getOrders(token: String): RequestState<AllOrderData> {
        try{
            val response = orderApi.getOrders("Bearer $token")
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<AllOrderData>>(it).message
                return RequestState.ERROR(errMsg)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }
}