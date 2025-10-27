package com.ptit.data.model.author.fetch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class FetchAllAuthorResponse{
    @Serializable
    data class Data(
        @SerialName("authors")
        val authors: List<AuthorResponseData>,
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