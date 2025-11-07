package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.coupon.CouponData

interface CouponRepository {
    suspend fun getCouponById(accessToken:String,id:Int): RequestState<CouponData>
    suspend fun getAllCoupons(accessToken:String): RequestState<List<CouponData>>
}