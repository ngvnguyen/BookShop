package com.ptit.data.model.permisson.fetch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class FetchAllPermissionResponse{
    @Serializable
    data class Data(
        @SerialName("page")
        val page: Int,
        @SerialName("page_size")
        val pageSize: Int,
        @SerialName("pages")
        val pages: Int,
        @SerialName("permissions")
        val permissions: List<PermissionResponseData>,
        @SerialName("total")
        val total: Int
    )
}