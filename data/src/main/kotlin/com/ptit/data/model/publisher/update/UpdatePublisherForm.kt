package com.ptit.data.model.publisher.update


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePublisherForm(
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