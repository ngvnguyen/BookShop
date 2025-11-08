package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.book.create.CreateBookForm
import com.ptit.data.model.book.fetch.BookResponseData
import com.ptit.data.model.book.fetch.FetchBookPagedResponse
import com.ptit.data.model.book.update.UpdateBookForm

interface BookRepository {
    suspend fun createBook(accessToken: String,bookForm: CreateBookForm): RequestState<Unit>
    suspend fun updateBook(accessToken: String,bookId:Int,bookForm: UpdateBookForm): RequestState<Unit>
    suspend fun searchBookPaged(
        accessToken:String,
        page:Int,
        pageSize:Int=20,
        name:String,
        categoryQuery: List<String>,
        authorQuery: List<String>,
        upperPrice:Int?=null,
        lowerPrice:Int?=null
    ): RequestState<FetchBookPagedResponse.Data>
    suspend fun getBookById(accessToken: String,bookId:Int): RequestState<BookResponseData>
}