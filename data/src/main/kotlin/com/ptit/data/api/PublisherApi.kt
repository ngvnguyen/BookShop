package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.publisher.create.CreatePublisherForm
import com.ptit.data.model.publisher.create.CreatePublisherResponse
import com.ptit.data.model.publisher.fetch.FetchAllPublisherResponse
import com.ptit.data.model.publisher.fetch.PublisherResponseData
import com.ptit.data.model.publisher.update.UpdatePublisherForm
import com.ptit.data.model.publisher.update.UpdatePublisherResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PublisherApi {
    @POST("api/v1/publishers")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body body: CreatePublisherForm
    ): Response<CreatePublisherResponse>

    @PUT("api/v1/publishers/{id}")
    suspend fun update(
        @Header("Authorization") token: String,
        @Body body: UpdatePublisherForm,
        @Path("id") id:Int
    ): Response<UpdatePublisherResponse>

    @DELETE("api/v1/publishers/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<String>

    @GET("api/v1/publishers/{id}")
    suspend fun getPublisherById(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<ResponseEntity<PublisherResponseData>>

    @GET("api/v1/publishers")
    suspend fun getAllPublisher(
        @Header("Authorization") token: String
    ): Response<ResponseEntity<FetchAllPublisherResponse.Data>>
}