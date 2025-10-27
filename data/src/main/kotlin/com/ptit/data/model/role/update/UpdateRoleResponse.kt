package com.ptit.data.model.role.update


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class UpdateRoleResponse {
    @Serializable
    data class Data(
        @SerialName("created_at")
        val createdAt: String?,
        @SerialName("created_by")
        val createdBy: String?,
        @SerialName("description")
        val description: String,
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("permissions")
        val permissions: List<Permission>,
        @SerialName("status")
        val status: String
    ) {
        @Serializable
        data class Permission(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String
        )
    }
}