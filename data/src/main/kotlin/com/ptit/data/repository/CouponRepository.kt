package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.coupon.CouponData
import com.ptit.data.model.coupon.CouponRequestForm

interface CouponRepository {
    suspend fun getCouponById(accessToken:String,id:String): RequestState<CouponData>
    suspend fun getAllCoupons(accessToken:String): RequestState<List<CouponData>>
    suspend fun createCoupon(accessToken: String,createCouponRequest: CouponRequestForm) : RequestState<String>
    suspend fun updateCoupon(accessToken: String,id:String,updateCouponRequest: CouponRequestForm) : RequestState<String>
    suspend fun deleteCoupon(accessToken: String,id:String) : RequestState<String>
    suspend fun getAllCouponsAdmin(accessToken:String): RequestState<List<CouponData>>
}