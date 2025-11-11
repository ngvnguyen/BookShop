package com.ptit.data.model.user.update


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserForm(
    @SerialName("avatar")
    val avatar: String?,
    @SerialName("date_of_birth")
    val dateOfBirth: String?,
    @SerialName("email")
    val email: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("name")
    val name: String,
    @SerialName("phone")
    val phone: String?,
    @SerialName("role")
    val role: Role,
    @SerialName("status")
    val status: String
) {
    @Serializable
    data class Role(
        @SerialName("id")
        val id: Int
    )
}