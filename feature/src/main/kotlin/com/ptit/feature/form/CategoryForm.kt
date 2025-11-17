package com.ptit.feature.form

import com.ptit.data.model.category.create.CreateCategoryForm
import com.ptit.data.model.category.fetch.CategoryResponseData
import com.ptit.data.model.category.update.UpdateCategoryForm

data class CategoryForm(
    val name:String="",
    val description:String="",
    val id:Int=0,
    val status: Status=Status.ACTIVE,
    val createdAt:String="",
    val updatedAt:String=""
){
    enum class Status(val title: String){
        ACTIVE("Active"),
        INACTIVE("Inactive")
    }
}

fun CategoryResponseData.toCategoryForm() = CategoryForm(
    name = name,
    description = description,
    id = id,
    status = CategoryForm.Status.valueOf(status),
    createdAt = createdAt,
    updatedAt = updatedAt?:""
)
fun CategoryForm.toCreateCategoryForm() = CreateCategoryForm(
    description = description,
    name = name,
    status = status.name
)

fun CategoryForm.toUpdateCategoryForm() = UpdateCategoryForm(
    description = description,
    name = name,
    status = status.name
)