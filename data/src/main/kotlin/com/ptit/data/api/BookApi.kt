package com.ptit.data.api

import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.book.create.CreateBookForm
import com.ptit.data.model.book.create.CreateBookResponse
import com.ptit.data.model.book.fetch.BookResponseData
import com.ptit.data.model.book.fetch.FetchBookPagedResponse
import com.ptit.data.model.book.update.UpdateBookForm
import com.ptit.data.model.book.update.UpdateBookResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApi {
    @POST("api/v1/books")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body body: CreateBookForm
    ): Response<ResponseEntity<CreateBookResponse.Data>>

    @PUT("api/v1/books/{id}")
    suspend fun update(
        @Header("Authorization") token: String,
        @Body body: UpdateBookForm,
        @Path("id") id:Int
    ): Response<ResponseEntity<UpdateBookResponse.Data>>

    @DELETE("api/v1/books/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<String>

    @GET("api/v1/books/{id}")
    suspend fun getBookById(
        @Header("Authorization") token: String,
        @Path("id") id:Int
    ): Response<ResponseEntity<BookResponseData>>

    @GET("api/v1/books")
    suspend fun getBookPaged(
        @Header("Authorization") token: String,
        @Query("page") page:Int=0,
        @Query("size") size:Int = 20,
        @Query("book") bookName:String?=null,
        @Query("category") categoryName:String?=null,
        @Query("author") authorName:String?=null
    ): Response<ResponseEntity<FetchBookPagedResponse.Data>>

    @GET("api/v1/books/newest")
    suspend fun getNewestBook(
        @Header("Authorization") token: String,
    ): Response<ResponseEntity<FetchBookPagedResponse.Data>>

    @GET("api/v1/books/discount")
    suspend fun getDiscountedBook(
        @Header("Authorization") token: String,
    ): Response<ResponseEntity<FetchBookPagedResponse.Data>>

}