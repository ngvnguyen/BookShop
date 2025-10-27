package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.role.create.CreateRoleForm
import com.ptit.data.model.role.create.CreateRoleResponse
import com.ptit.data.model.role.fetch.FetchAllRoleResponse
import com.ptit.data.model.role.fetch.RoleResponseData
import com.ptit.data.model.role.update.UpdateRoleForm
import com.ptit.data.model.role.update.UpdateRoleResponse
import com.ptit.data.model.user.create.CreateUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoleApi {
    @POST("api/v1/roles")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body body: CreateRoleForm
    ): Response<ResponseEntity<CreateUserResponse.Data>>

    @PUT("api/v1/roles/{id}")
    suspend fun update(
        @Header("Authorization") token: String,
        @Body body: UpdateRoleForm,
        @Path("id") id:Int
    ): Response<ResponseEntity<UpdateRoleResponse.Data>>

    @DELETE("api/v1/roles/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<String>

    @GET("api/v1/roles/{id}")
    suspend fun getRoleById(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<ResponseEntity<RoleResponseData>>

    @GET("api/v1/roles")
    suspend fun getAllRole(
        @Header("Authorization") token: String
    ): Response<ResponseEntity<FetchAllRoleResponse.Data>>
}