package com.ptit.data.model.role.create


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoleForm(
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