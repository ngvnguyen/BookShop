package com.ptit.data.model.category.create


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCategoryForm(
    @SerialName("description")
    val description: String,
    @SerialName("name")
    val name: String
)