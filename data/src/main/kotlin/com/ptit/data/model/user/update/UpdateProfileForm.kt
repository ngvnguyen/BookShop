package com.ptit.data.model.user.update


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileForm(
    @SerialName("avatar")
    val avatar: String?=null,
    @SerialName("date_of_birth")
    val dateOfBirth: String?,
    @SerialName("gender")
    val gender: String,
    @SerialName("name")
    val name: String,
    @SerialName("phone")
    val phone: String
)