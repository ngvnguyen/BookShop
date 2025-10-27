package com.ptit.data.model.publisher.fetch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PublisherResponseData(
    @SerialName("address")
    val address: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("created_by")
    val createdBy: String,
    @SerialName("email")
    val email: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("status")
    val status: String
)