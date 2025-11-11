package com.ptit.data.model.auth.login


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class LoginResponse {
    @Serializable
    data class Data(
        @SerialName("access_token")
        val accessToken: String,
        @SerialName("refresh_token")
        val refreshToken:String,
        @SerialName("user")
        val user: User
    ) {
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
}