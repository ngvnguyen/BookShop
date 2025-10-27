package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.auth.createaccount.SignUpBody
import com.ptit.data.model.auth.getaccount.AccountResponse
import com.ptit.data.model.auth.login.LoginForm
import com.ptit.data.model.auth.login.LoginResponse

interface AuthRepository {
    suspend fun login(loginForm: LoginForm): RequestState<Pair<String?, LoginResponse.Data>>
    suspend fun loginWithRefreshToken(token: String): RequestState<Pair<String?, LoginResponse.Data>>
    suspend fun signUp(signUpBody: SignUpBody): RequestState<String>
    suspend fun getAccount(accessToken:String): RequestState<AccountResponse.Data>
    suspend fun signOut(accessToken:String): RequestState<String>
    suspend fun sendOTP(email:String): RequestState<String>
    suspend fun verifyOTP(otp:String): RequestState<String>
    suspend fun resetPassword(resetToken:String,newPassword:String): RequestState<String>
}