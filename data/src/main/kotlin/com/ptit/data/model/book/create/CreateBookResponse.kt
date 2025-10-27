package com.ptit.data.model.book.create


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class CreateBookResponse{
    @Serializable
    data class Data(
        @SerialName("author")
        val author: Author,
        @SerialName("category")
        val category: Category,
        @SerialName("description")
        val description: String,
        @SerialName("discount")
        val discount: Double,
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
}