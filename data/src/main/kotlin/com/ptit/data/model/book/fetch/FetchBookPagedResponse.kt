package com.ptit.data.model.book.fetch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class FetchBookPagedResponse{
    @Serializable
    data class Data(
        @SerialName("books")
        val books: List<BookResponseData>,
        @SerialName("page")
        val page: Int,
        @SerialName("page_size")
        val pageSize: Int,
        @SerialName("pages")
        val pages: Int,
        @SerialName("total")
        val total: Int
    )

}