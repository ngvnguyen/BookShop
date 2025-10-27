package com.ptit.data.model.publisher.create


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePublisherForm(
    @SerialName("address")
    val address: String,
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("status")
    val status: String
)