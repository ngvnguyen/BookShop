package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.CouponApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.coupon.AllCouponResponse
import com.ptit.data.model.coupon.CouponData
import com.ptit.data.model.coupon.CouponRequestForm
import com.ptit.data.repository.CouponRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class CouponRepositoryImpl(private val couponApi: CouponApi): CouponRepository {
    override suspend fun getCouponById(accessToken: String, id: String): RequestState<CouponData> {
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

    override suspend fun createCoupon(
        accessToken: String,
        createCouponRequest: CouponRequestForm
    ): RequestState<String> {
        try {
            val response = couponApi.createCoupon("Bearer $accessToken",createCouponRequest)
            if (response.isSuccessful){
                val data = response.body()
                data?.let { return RequestState.SUCCESS(it.message) }
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

    override suspend fun updateCoupon(
        accessToken: String,
        id: String,
        updateCouponRequest: CouponRequestForm
    ): RequestState<String> {
        try {
            val response = couponApi.updateCoupon("Bearer $accessToken",updateCouponRequest,id)
            if (response.isSuccessful){
                val data = response.body()
                data?.let { return RequestState.SUCCESS(it.message) }
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

    override suspend fun deleteCoupon(accessToken: String, id: String): RequestState<String> {
        try {
            val response = couponApi.deleteCoupon("Bearer $accessToken",id)
            if (response.isSuccessful){
                return RequestState.SUCCESS("Deleted")
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                return RequestState.ERROR(it)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun getAllCouponsAdmin(accessToken: String): RequestState<List<CouponData>> {
        try {
            val response = couponApi.getAllCouponsAdmin("Bearer $accessToken")
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