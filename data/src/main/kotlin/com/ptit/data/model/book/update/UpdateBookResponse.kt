package com.ptit.data.model.book.update


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class UpdateBookResponse {
    @Serializable
    data class Data(
        @SerialName("author")
        val author: Author,
        @SerialName("category")
        val category: Category,
        @SerialName("created_at")
        val createdAt: String,
        @SerialName("update_at")
        val updatedAt: String?,
        @SerialName("description")
        val description: String,
        @SerialName("discount")
        val discount: Double,
        @SerialName("final_price")
        val finalPrice: Double,
        @SerialName("id")
        val id: Int,
        @SerialName("image_url")
        val image: String,
        @SerialName("language")
        val language: String,
        @SerialName("name")
        val name: String,
        @SerialName("price")
        val price: Double,
        @SerialName("publisher")
        val publisher: Publisher,
        @SerialName("quantity")
        val quantity: Int,
        @SerialName("status")
        val status: String
    ) {
        @Serializable
        data class Author(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String
        )

        @Serializable
        data class Category(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String
        )

        @Serializable
        data class Publisher(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String
        )
    }
}