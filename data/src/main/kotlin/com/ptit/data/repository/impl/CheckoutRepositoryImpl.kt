package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.CheckoutApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.cart.CartData
import com.ptit.data.model.checkout.CheckoutData
import com.ptit.data.model.checkout.CheckoutRequest
import com.ptit.data.repository.CheckoutRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class CheckoutRepositoryImpl(private val checkoutApi: CheckoutApi): CheckoutRepository {
    override suspend fun checkout(
        token: String,
        checkoutRequest: CheckoutRequest
    ): RequestState<CheckoutData> {
        try{
            val response = checkoutApi.checkout("Bearer $token",checkoutRequest)
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<CheckoutData>>(it).message
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