package com.ptit.data.repository.impl

import android.util.Log
import com.ptit.data.RequestState
import com.ptit.data.api.AuthApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.auth.login.LoginForm
import com.ptit.data.model.auth.createaccount.SignUpBody
import com.ptit.data.model.auth.getaccount.AccountResponse
import com.ptit.data.model.auth.login.LoginResponse
import com.ptit.data.model.auth.resetpassword.Email
import com.ptit.data.model.auth.resetpassword.OTP
import com.ptit.data.model.auth.resetpassword.ResetPasswordForm
import com.ptit.data.repository.AuthRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class AuthRepositoryImpl(private val authApi: AuthApi): AuthRepository {
    override suspend fun login(loginForm: LoginForm): RequestState<Pair<String?, LoginResponse.Data>>  {
        try{
            val response = authApi.login(loginForm)

            if (response.isSuccessful){

                val loginBody = response.body()
                val refreshToken = response.headers()["Set-Cookie"]?.split(";")
                    ?.firstOrNull{it.startsWith("refresh_token=")}
                    ?.substringAfter("refresh_token=")
                loginBody?.data?.let { return RequestState.SUCCESS(Pair(refreshToken,it)) }
            }
            val errorBody = response.errorBody()?.string()
            errorBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<LoginResponse.Data>>(it)
                    .message
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

    override suspend fun loginWithRefreshToken(token: String): RequestState<Pair<String?, LoginResponse.Data>> {
        try{
            val response = authApi.loginWithRefreshToken("refresh_token=$token")
            if (response.isSuccessful){
                val loginBody = response.body()
                val refreshToken = response.headers()["Set-Cookie"]?.split(";")
                    ?.firstOrNull{it.startsWith("refresh_token=")}
                    ?.substringAfter("refresh_token=")
                loginBody?.data?.let { return RequestState.SUCCESS(Pair(refreshToken,it)) }
            }
            val errorBody = response.errorBody()?.string()
            errorBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<LoginResponse.Data>>(it).message

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

    override suspend fun signUp(signUpBody: SignUpBody): RequestState<String> {
        try{
            val response = authApi.register(signUpBody)

            val msg = response.body()?: response.errorBody()?.string() ?: "Unknown error"

            if (response.isSuccessful) return RequestState.SUCCESS(msg)
            return RequestState.ERROR(msg)
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun getAccount(accessToken: String): RequestState<AccountResponse.Data> {
        try {
            val response = authApi.getAccount("Bearer $accessToken")
            if (response.isSuccessful){
                val accountResponse = response.body()
                accountResponse?.data?.let { return RequestState.SUCCESS(it) }
            }
            response.errorBody()?.string()?.let {json->
                val errorBody = Json.decodeFromString<ResponseEntity<AccountResponse.Data>>(json)
                errorBody.error?.let{return RequestState.ERROR(it)}
            }
            return RequestState.ERROR("Unknown error")

        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun signOut(accessToken: String): RequestState<String> {
        try{
            val signOutResponse = authApi.signOut("Bearer $accessToken")
            if (signOutResponse.isSuccessful){
                return RequestState.SUCCESS("Sign out successfully")
            }
            signOutResponse.errorBody()?.let { errBody->
                val signOutForm = Json.decodeFromString<ResponseEntity<Nothing>>(errBody.string())
                return RequestState.ERROR(signOutForm.message)
            }
            return RequestState.ERROR("Sign out failed: Unknown Error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }

    }

    override suspend fun sendOTP(email: String): RequestState<String> {
        try{
            val response = authApi.sendOTP(Email(email))
            if (response.isSuccessful) return RequestState.SUCCESS(response.body()?:"Success")
            response.errorBody()?.string()?.let {
                return RequestState.ERROR(it)
            }
            return RequestState.ERROR("Email does not exist")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun verifyOTP(otp: String): RequestState<String> {
        try{
            val response = authApi.verifyOTP(OTP(otp))
            if (response.isSuccessful) return RequestState.SUCCESS(response.body()?:"Success")

            return RequestState.ERROR("Wrong OTP")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun resetPassword(
        resetToken: String,
        newPassword: String
    ): RequestState<String> {
        try{
            val response = authApi.resetPassword(ResetPasswordForm(
                resetToken = resetToken,
                newPassword = newPassword,
                confirmNewPassword = newPassword
            ))
            if (response.isSuccessful) return RequestState.SUCCESS(response.body()?:"Success")
            response.errorBody()?.string()?.let {
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
}