package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.CategoryApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.author.fetch.FetchAllAuthorResponse
import com.ptit.data.model.category.fetch.CategoryResponseData
import com.ptit.data.model.category.fetch.FetchAllCategoryResponse
import com.ptit.data.repository.CategoryRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class CategoryRepositoryImpl(private val categoryApi: CategoryApi): CategoryRepository {
    override suspend fun getAllCategory(accessToken: String): RequestState<List<CategoryResponseData>> {
        try {
            val response = categoryApi.getAllCategory("Bearer $accessToken")
            if (response.isSuccessful){
                val allAuthor = response.body()?.data
                allAuthor?.let{return RequestState.SUCCESS(it.categories) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<FetchAllCategoryResponse.Data>>(it).message
                return RequestState.ERROR(errMsg)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }
}