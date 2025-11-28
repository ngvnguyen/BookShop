package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.coupon.AllCouponResponse
import com.ptit.data.model.coupon.CouponData
import com.ptit.data.model.coupon.CouponRequestForm
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CouponApi {

    @POST("/api/v1/coupons")
    suspend fun createCoupon(
        @Header("Authorization") token: String,
        @Body body: CouponRequestForm
    ): Response<ResponseEntity<CouponData>>

    @PUT("/api/v1/coupons/{id}")
    suspend fun updateCoupon(
        @Header("Authorization") token: String,
        @Body body: CouponRequestForm,
        @Path("id") id: String
    ): Response<ResponseEntity<CouponData>>

    @DELETE("/api/v1/coupons/{id}")
    suspend fun deleteCoupon(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<String>

    @GET("/api/v1/coupons")
    suspend fun getAllCoupons(
        @Header("Authorization") token: String,
    ): Response<ResponseEntity<AllCouponResponse>>

    @GET("/api/v1/coupons/{id}")
    suspend fun getCouponById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ResponseEntity<CouponData>>

    @GET("/api/v1/admin/coupons/{id}")
    suspend fun getCouponAdminById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ResponseEntity<CouponData>>

    @GET("/api/v1/admin/coupons")
    suspend fun getAllCouponsAdmin(
        @Header("Authorization") token: String,
    ): Response<ResponseEntity<AllCouponResponse>>

}