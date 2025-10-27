package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.category.fetch.CategoryResponseData

interface CategoryRepository {
    suspend fun getAllCategory(accessToken:String): RequestState<List<CategoryResponseData>>
}