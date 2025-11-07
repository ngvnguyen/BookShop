package com.ptit.data.model.author.fetch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorResponseData(
    @SerialName("biography")
    val biography: String,
    @SerialName("country")
    val country: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("update_at")
    val updatedAt: String?,
    @SerialName("date_of_birth")
    val dateOfBirth: String?,
    @SerialName("gender")
    val gender: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("status")
    val status: String
)