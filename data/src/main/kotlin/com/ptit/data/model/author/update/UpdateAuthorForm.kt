package com.ptit.data.model.author.update


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAuthorForm(
    @SerialName("biography")
    val biography: String,
    @SerialName("country")
    val country: String,
    @SerialName("date_of_birth")
    val dateOfBirth: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("name")
    val name: String
)