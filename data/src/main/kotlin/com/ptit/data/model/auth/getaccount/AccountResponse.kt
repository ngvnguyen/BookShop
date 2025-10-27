package com.ptit.data.model.auth.getaccount


import com.ptit.data.model.ResponseEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class AccountResponse{
    @Serializable
    data class Data(
        @SerialName("user")
        val user: User
    )
    @Serializable
    data class User(
        @SerialName("email")
        val email: String,
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("role")
        val role: Role,
        @SerialName("status")
        val status: String?
    )
    @Serializable
    data class Role(
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String
    )
}