package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.CouponApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.cart.CartData
import com.ptit.data.model.coupon.AllCouponResponse
import com.ptit.data.model.coupon.CouponData
import com.ptit.data.repository.CouponRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class CouponRepositoryImpl(private val couponApi: CouponApi): CouponRepository {
    override suspend fun getCouponById(accessToken: String, id: Int): RequestState<CouponData> {
        try {
            val response = couponApi.getCouponById("Bearer $accessToken",id)
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<CouponData>>(it).message
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

    override suspend fun getAllCoupons(accessToken: String): RequestState<List<CouponData>> {
        try {
            val response = couponApi.getAllCoupons("Bearer $accessToken")
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it.coupons) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<AllCouponResponse>>(it).message
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