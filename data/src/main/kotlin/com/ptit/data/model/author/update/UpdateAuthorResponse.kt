package com.ptit.data.model.author.update


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class UpdateAuthorResponse {
    @Serializable
    data class Data(
        @SerialName("biography")
        val biography: String,
        @SerialName("country")
        val country: String,
        @SerialName("created_at")
        val createdAt: String,
        @SerialName("created_by")
        val createdBy: String,
        @SerialName("date_of_birth")
        val dateOfBirth: String,
        @SerialName("gender")
        val gender: String,
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("status")
        val status: String
    )
}