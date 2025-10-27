package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.author.create.CreateAuthorForm
import com.ptit.data.model.author.create.CreateAuthorResponse
import com.ptit.data.model.author.fetch.AuthorResponseData
import com.ptit.data.model.author.fetch.FetchAllAuthorResponse
import com.ptit.data.model.author.update.UpdateAuthorForm
import com.ptit.data.model.author.update.UpdateAuthorResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthorApi {
    @POST("api/v1/authors")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body body: CreateAuthorForm
    ): Response<CreateAuthorResponse>

    @PUT("api/v1/authors/{id}")
    suspend fun update(
        @Header("Authorization") token: String,
        @Body body: UpdateAuthorForm,
        @Path("id") id:Int
    ): Response<UpdateAuthorResponse>

    @DELETE("api/v1/authors/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<String>

    @GET("api/v1/authors/{id}")
    suspend fun getAuthorById(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<ResponseEntity<AuthorResponseData>>

    @GET("api/v1/authors")
    suspend fun getAllAuthor(
        @Header("Authorization") token: String
    ): Response<ResponseEntity<FetchAllAuthorResponse.Data>>
}