package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.permisson.create.CreatePermissionForm
import com.ptit.data.model.permisson.create.CreatePermissionResponse
import com.ptit.data.model.permisson.fetch.FetchAllPermissionResponse
import com.ptit.data.model.permisson.fetch.PermissionResponseData
import com.ptit.data.model.permisson.update.UpdatePermissionForm
import com.ptit.data.model.permisson.update.UpdatePermissionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PermissionApi {
    @POST("api/v1/permissions")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body body: CreatePermissionForm
    ): Response<ResponseEntity<CreatePermissionResponse.Data>>

    @PUT("api/v1/permissions/{id}")
    suspend fun update(
        @Header("Authorization") token: String,
        @Body body: UpdatePermissionForm,
        @Path("id") id:Int
    ): Response<ResponseEntity<UpdatePermissionResponse.Data>>

    @DELETE("api/v1/permissions/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<String>

    @GET("api/v1/permissions/{id}")
    suspend fun getPermissionById(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<ResponseEntity<PermissionResponseData>>

    @GET("api/v1/permissions")
    suspend fun getAllPermission(
        @Header("Authorization") token: String,
        @Query("size") pageSize:Int = 80
    ): Response<ResponseEntity<FetchAllPermissionResponse.Data>>
}