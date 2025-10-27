package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.user.create.CreateUserForm
import com.ptit.data.model.user.create.CreateUserResponse
import com.ptit.data.model.user.fetch.FetchUserPagedResponse
import com.ptit.data.model.user.fetch.UserResponseData
import com.ptit.data.model.user.update.UpdateProfileForm
import com.ptit.data.model.user.update.UpdateUserForm
import com.ptit.data.model.user.update.UpdateUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @POST("api/v1/users")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body body: CreateUserForm
    ): Response<ResponseEntity<CreateUserResponse.Data>>

    @PUT("api/v1/users/{id}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body body: UpdateUserForm,
        @Path("id") id:Int
    ): Response<ResponseEntity<UpdateUserResponse.Data>>

    @PUT("api/v1/users/{id}")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body body: UpdateProfileForm,
        @Path("id") id:Int
    ): Response<ResponseEntity<UpdateUserResponse.Data>>

    @DELETE("api/v1/users/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<String>

    @GET("api/v1/users/{id}")
    suspend fun getUserById(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<ResponseEntity<UserResponseData>>

    @GET("api/v1/users")
    suspend fun getUserPaged(
        @Header("Authorization") token: String,
        @Query("page") page:Int,
        @Query("user") userName:String?=null
    ): Response<ResponseEntity<FetchUserPagedResponse.Data>>
}