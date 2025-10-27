package com.ptit.data.model.user.create


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class CreateUserResponse private constructor(){
    @Serializable
    data class Data(
        @SerialName("address")
        val address: String?,
        @SerialName("avatar_url")
        val avatarUrl: String,
        @SerialName("created_at")
        val createdAt: String,
        @SerialName("created_by")
        val createdBy: String,
        @SerialName("date_of_birth")
        val dateOfBirth: String?=null,
        @SerialName("email")
        val email: String,
        @SerialName("gender")
        val gender: String,
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("phone")
        val phone: String,
        @SerialName("role")
        val role: Role,
        @SerialName("status")
        val status: String
    ) {
        @Serializable
        data class Role(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String
        )
    }
}