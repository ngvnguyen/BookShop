package com.ptit.data.model.role.fetch


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class FetchAllRoleResponse {
    @Serializable
    data class Data(
        @SerialName("page")
        val page: Int,
        @SerialName("page_size")
        val pageSize: Int,
        @SerialName("pages")
        val pages: Int,
        @SerialName("roles")
        val roles: List<RoleResponseData>,
        @SerialName("total")
        val total: Int
    )
}