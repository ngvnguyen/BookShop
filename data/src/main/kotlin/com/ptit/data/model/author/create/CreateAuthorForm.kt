package com.ptit.data.model.author.create


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAuthorForm(
    @SerialName("biography")
    val biography: String,
    @SerialName("country")
    val country: String,
    @SerialName("dateOfBirth")
    val dateOfBirth: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("name")
    val name: String
)