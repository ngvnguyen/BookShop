package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.category.create.CreateCategoryForm
import com.ptit.data.model.category.create.CreateCategoryResponse
import com.ptit.data.model.category.fetch.CategoryResponseData
import com.ptit.data.model.category.fetch.FetchAllCategoryResponse
import com.ptit.data.model.category.update.UpdateCategoryForm
import com.ptit.data.model.category.update.UpdateCategoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoryApi {
    @POST("api/v1/categories")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body body: CreateCategoryForm
    ): Response<ResponseEntity<CreateCategoryResponse.Data>>

    @PUT("api/v1/categories/{id}")
    suspend fun update(
        @Header("Authorization") token: String,
        @Body body: UpdateCategoryForm,
        @Path("id") id:Int
    ): Response<ResponseEntity<UpdateCategoryResponse.Data>>

    @DELETE("api/v1/categories/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<Unit>

    @GET("api/v1/categories/{id}")
    suspend fun getCategoryById(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<ResponseEntity<CategoryResponseData>>

    @GET("api/v1/categories")
    suspend fun getAllCategory(
        @Header("Authorization") token: String
    ): Response<ResponseEntity<FetchAllCategoryResponse.Data>>
}