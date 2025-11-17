package com.ptit.data.model.category.update


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateCategoryForm(
    @SerialName("description")
    val description: String,
    @SerialName("name")
    val name: String,
    val status:String
)