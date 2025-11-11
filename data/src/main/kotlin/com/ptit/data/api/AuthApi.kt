package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.auth.changepassword.ChangePasswordForm
import com.ptit.data.model.auth.login.LoginForm
import com.ptit.data.model.auth.createaccount.SignUpBody
import com.ptit.data.model.auth.getaccount.AccountResponse
import com.ptit.data.model.auth.login.LoginResponse
import com.ptit.data.model.auth.resetpassword.Email
import com.ptit.data.model.auth.resetpassword.OTP
import com.ptit.data.model.auth.resetpassword.ResetPasswordForm
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface AuthApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body body:LoginForm): Response<ResponseEntity<LoginResponse.Data>>

    @POST("api/v1/auth/register")
    suspend fun register(@Body body: SignUpBody):Response<String>

    @GET("api/v1/auth/account")
    suspend fun getAccount(
        @Header("Authorization") token:String
    ): Response<ResponseEntity<AccountResponse.Data>>

    @POST("api/v1/auth/refresh")
    suspend fun loginWithRefreshToken(@Header("refresh-token") refreshToken:String)
    : Response<ResponseEntity<LoginResponse.Data>>

    @POST("api/v1/auth/logout")
    suspend fun signOut(@Header("Authorization") token:String): Response<ResponseEntity<Unit>>

    @POST("api/v1/auth/forgot-password")
    suspend fun sendOTP(@Body body: Email): Response<String>

    @POST("api/v1/auth/verify-otp")
    suspend fun verifyOTP(@Body body: OTP): Response<String>

    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordForm): Response<String>

    @POST("/api/v1/auth/password-change")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body body: ChangePasswordForm
    ): Response<String>
}