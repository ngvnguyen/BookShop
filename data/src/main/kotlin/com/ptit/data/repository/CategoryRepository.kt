package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.category.create.CreateCategoryForm
import com.ptit.data.model.category.fetch.CategoryResponseData
import com.ptit.data.model.category.update.UpdateCategoryForm

interface CategoryRepository {
    suspend fun getAllCategory(accessToken:String): RequestState<List<CategoryResponseData>>
    suspend fun createCategory(accessToken: String,createCategoryForm: CreateCategoryForm): RequestState<String>
    suspend fun updateCategory(accessToken: String,id:Int,updateCategoryForm: UpdateCategoryForm): RequestState<String>
    suspend fun deleteCategory(accessToken: String,id:Int): RequestState<String>
}