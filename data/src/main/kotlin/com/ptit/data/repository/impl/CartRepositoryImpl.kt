package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.CartApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.book.create.CreateBookResponse
import com.ptit.data.model.cart.AddToCartForm
import com.ptit.data.model.cart.CartData
import com.ptit.data.repository.CartRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class CartRepositoryImpl(private val cartApi: CartApi): CartRepository {
    override suspend fun getCart(accessToken: String): RequestState<CartData> {
        try{
            val response = cartApi.getCart("Bearer $accessToken")
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<CartData>>(it).message
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

    override suspend fun updateCartItem(
        accessToken: String,
        productId: Int,
        quantity: Int
    ): RequestState<CartData> {
        try{
            val response = cartApi.updateItemCart("Bearer $accessToken",productId,quantity)
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<CartData>>(it).message
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

    override suspend fun addItemToCart(
        accessToken: String,
        addToCartForm: AddToCartForm
    ): RequestState<Int> {
        try{
            val response = cartApi.addItemToCart("Bearer $accessToken",addToCartForm)
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it.id) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<CartData>>(it).message
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

    override suspend fun deleteCartItem(
        accessToken: String,
        productId: Int
    ): RequestState<Unit> {
        try{
            val response = cartApi.deleteCartItem("Bearer $accessToken",productId)
            if (response.isSuccessful){
                return RequestState.SUCCESS(Unit)
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<Unit>>(it).message
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