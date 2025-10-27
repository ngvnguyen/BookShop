package com.ptit.data.model.category.fetch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
class FetchAllCategoryResponse {
    @Serializable
    data class Data(
        @SerialName("categories")
        val categories: List<CategoryResponseData>,
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