package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.UserApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.user.create.CreateUserForm
import com.ptit.data.model.user.create.CreateUserResponse
import com.ptit.data.model.user.fetch.FetchUserPagedResponse
import com.ptit.data.model.user.fetch.UserResponseData
import com.ptit.data.model.user.update.UpdateProfileForm
import com.ptit.data.model.user.update.UpdateUserForm
import com.ptit.data.model.user.update.UpdateUserResponse
import com.ptit.data.repository.UserRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class UserRepositoryImpl(private val userApi: UserApi): UserRepository {
    override suspend fun createUser(
        accessToken:String,
        createUserForm: CreateUserForm
    ): RequestState<Int> {
        try {
            val response = userApi.create("Bearer $accessToken", createUserForm)
            if (response.isSuccessful){
                val createUserData = response.body()?.data
                createUserData?.let { return RequestState.SUCCESS(it.id) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<CreateUserResponse.Data>>(body).message
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

    override suspend fun updateUser(
        accessToken: String,
        id: Int,
        updateUserForm: UpdateUserForm
    ): RequestState<Int> {
        try {
            val response = userApi.updateUser("Bearer $accessToken",updateUserForm,id)
            if (response.isSuccessful){
                val updateUserResponse = response.body()?.data
                updateUserResponse?.let { return RequestState.SUCCESS(it.id) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<UpdateUserResponse.Data>>(body).message
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

    override suspend fun updateProfile(
        accessToken: String,
        id: Int,
        updateProfileForm: UpdateProfileForm
    ): RequestState<Int> {
        try {
            val response = userApi.updateProfile("Bearer $accessToken",updateProfileForm,id)
            if (response.isSuccessful){
                val updateProfileResponse = response.body()?.data
                updateProfileResponse?.let { return RequestState.SUCCESS(it.id) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<UpdateUserResponse.Data>>(body).message
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

    override suspend fun deleteUser(
        accessToken: String,
        id: Int
    ): RequestState<Int> {
        try {
            val response = userApi.delete("Bearer $accessToken", id)
            if (response.isSuccessful){
                return RequestState.SUCCESS(id)
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<String>>(body).message
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

    override suspend fun getUserById(
        accessToken: String,
        id: Int
    ): RequestState<UserResponseData> {
        try {
            val response = userApi.getUserById("Bearer $accessToken", id)
            if (response.isSuccessful){
                val userResponseData = response.body()?.data
                userResponseData?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<UserResponseData>>(body).message
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

    override suspend fun getUserPaged(
        accessToken: String,
        page: Int,
        userName:String?
    ): RequestState<FetchUserPagedResponse.Data> {
        try {
            val query = userName?.let { "name~$it" }
            val response = userApi.getUserPaged("Bearer $accessToken", page,query)
            if (response.isSuccessful){
                val fetchUserPagedResponseData = response.body()?.data
                fetchUserPagedResponseData?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json
                    .decodeFromString<ResponseEntity<FetchUserPagedResponse.Data>>(body)
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

}