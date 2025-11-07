package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.address.AddressData
import com.ptit.data.model.address.AddressRequestForm
import com.ptit.data.model.address.AllAddressResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AddressApi {
    @GET("/api/v1/addresses/{id}")
    suspend fun getAddressById(
        @Header("Authorization") token:String,
        @Path("id") id:Int
    ): Response<ResponseEntity<AddressData>>

    @GET("/api/v1/addresses")
    suspend fun getAllAddress(
        @Header("Authorization") token:String
    ): Response<ResponseEntity<AllAddressResponse>>

    @PUT("/api/v1/addresses/{id}")
    suspend fun updateAddressById(
        @Header("Authorization") token:String,
        @Path("id") id:Int,
        @Body body : AddressRequestForm
    ): Response<ResponseEntity<AddressData>>

    @DELETE("/api/v1/addresses/{id}")
    suspend fun deleteAddressById(
        @Header("Authorization") token:String,
        @Path("id") id:Int
    ): Response<ResponseEntity<Unit>>

    @POST("/api/v1/addresses")
    suspend fun createNewAddress(
        @Header("Authorization") token:String,
        @Body body: AddressRequestForm
    ): Response<ResponseEntity<AddressData>>
}