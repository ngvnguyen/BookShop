package com.ptit.data.model.user.update


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileForm(
    @SerialName("address")
    val address: String?,
    @SerialName("avatar")
    val avatar: String?=null,
    @SerialName("dateOfBirth")
    val dateOfBirth: String?,
    @SerialName("gender")
    val gender: String,
    @SerialName("name")
    val name: String,
    @SerialName("phone")
    val phone: String
)