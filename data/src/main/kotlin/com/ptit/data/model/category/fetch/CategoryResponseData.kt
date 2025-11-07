package com.ptit.data.model.category.fetch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CategoryResponseData(
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("update_at")
    val updatedAt: String?,
    @SerialName("description")
    val description: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("status")
    val status: String
)