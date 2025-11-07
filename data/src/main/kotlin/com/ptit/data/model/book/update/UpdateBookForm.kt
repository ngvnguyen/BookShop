package com.ptit.data.model.book.update


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateBookForm(
    @SerialName("author")
    val author: Author,
    @SerialName("category")
    val category: Category,
    @SerialName("description")
    val description: String,
    @SerialName("discount")
    val discount: Double,
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
        val id: Int
    )

    @Serializable
    data class Category(
        @SerialName("id")
        val id: Int
    )

    @Serializable
    data class Publisher(
        @SerialName("id")
        val id: Int
    )
}