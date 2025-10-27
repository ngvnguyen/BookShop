package com.ptit.data.model.category.create


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class CreateCategoryResponse{
    @Serializable
    data class Data(
        @SerialName("created_at")
        val createdAt: String,
        @SerialName("created_by")
        val createdBy: String,
        @SerialName("description")
        val description: String,
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("status")
        val status: String
    )
}