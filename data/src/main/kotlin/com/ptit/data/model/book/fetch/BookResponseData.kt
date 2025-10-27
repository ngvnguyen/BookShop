package com.ptit.data.model.book.fetch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BookResponseData(
    @SerialName("author")
    val author: Author,
    @SerialName("category")
    val category: Category,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("created_by")
    val createdBy: String,
    @SerialName("description")
    val description: String,
    @SerialName("discount")
    val discount: Double,
    @SerialName("final_price")
    val finalPrice: Double,
    @SerialName("id")
    val id: Int,
    @SerialName("image")
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
    val status: String,
    @SerialName("title")
    val title: String
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