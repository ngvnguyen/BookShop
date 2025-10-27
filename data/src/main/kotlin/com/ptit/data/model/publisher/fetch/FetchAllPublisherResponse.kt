package com.ptit.data.model.publisher.fetch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class FetchAllPublisherResponse {
    @Serializable
    data class Data(
        @SerialName("page")
        val page: Int,
        @SerialName("page_size")
        val pageSize: Int,
        @SerialName("pages")
        val pages: Int,
        @SerialName("publishers")
        val publishers: List<PublisherResponseData>,
        @SerialName("total")
        val total: Int
    )
}