package com.ptit.data.model.role.update


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRoleForm(
    @SerialName("description")
    val description: String,
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
        val id: Int
    )
}