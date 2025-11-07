package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.BookApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.book.create.CreateBookForm
import com.ptit.data.model.book.create.CreateBookResponse
import com.ptit.data.model.book.fetch.BookResponseData
import com.ptit.data.model.book.fetch.FetchBookPagedResponse
import com.ptit.data.model.book.update.UpdateBookForm
import com.ptit.data.model.book.update.UpdateBookResponse
import com.ptit.data.repository.BookRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class BookRepositoryImpl(private val bookApi: BookApi): BookRepository {
    override suspend fun createBook(
        accessToken: String,
        bookForm: CreateBookForm
    ): RequestState<Unit> {
        try{
            val response = bookApi.create(
                token = "Bearer $accessToken",
                body = bookForm
            )
            if (response.isSuccessful){
                return RequestState.SUCCESS(Unit)
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<CreateBookResponse.Data>>(it).message
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

    override suspend fun updateBook(
        accessToken: String,
        bookId: Int,
        bookForm: UpdateBookForm
    ): RequestState<Unit> {
        try{
            val response = bookApi.update(
                token = "Bearer $accessToken",
                body = bookForm,
                id = bookId
            )
            if (response.isSuccessful){
                return RequestState.SUCCESS(Unit)
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<UpdateBookResponse.Data>>(it).message
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

    override suspend fun searchBookPaged(
        accessToken: String,
        page: Int,
        name: String,
        category: String,
        author: String
    ): RequestState<FetchBookPagedResponse.Data> {
        try {
            val nameQuery = "name~${name}"
            val categoryQuery = "name~${category}"
            val authorQuery = "name~${author}"
            val response = bookApi.getBookPaged("Bearer $accessToken",page,nameQuery,categoryQuery,authorQuery)
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<FetchBookPagedResponse.Data>>(body).message
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

    override suspend fun getBookById(
        accessToken: String,
        bookId: Int
    ): RequestState<BookResponseData> {
        try {

            val response = bookApi.getBookById("Bearer $accessToken",bookId)
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<BookResponseData>>(body).message
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