package com.ptit.data.model.user.fetch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class FetchUserPagedResponse private constructor(){
    @Serializable
    data class Data(
        @SerialName("page")
        val page: Int,
        @SerialName("page_size")
        val pageSize: Int,
        @SerialName("pages")
        val pages: Int,
        @SerialName("total")
        val total: Int,
        @SerialName("users")
        val users: List<UserResponseData>
    )
}