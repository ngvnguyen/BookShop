package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.CategoryApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.author.fetch.FetchAllAuthorResponse
import com.ptit.data.model.category.create.CreateCategoryForm
import com.ptit.data.model.category.create.CreateCategoryResponse
import com.ptit.data.model.category.fetch.CategoryResponseData
import com.ptit.data.model.category.fetch.FetchAllCategoryResponse
import com.ptit.data.model.category.update.UpdateCategoryForm
import com.ptit.data.model.category.update.UpdateCategoryResponse
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

    override suspend fun createCategory(
        accessToken: String,
        createCategoryForm: CreateCategoryForm
    ): RequestState<String> {
        try {
            val response = categoryApi.create("Bearer $accessToken",createCategoryForm)
            if (response.isSuccessful){
                return RequestState.SUCCESS("Create category successfully")
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<CreateCategoryResponse.Data>>(it).message
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

    override suspend fun updateCategory(
        accessToken: String,
        id: Int,
        updateCategoryForm: UpdateCategoryForm
    ): RequestState<String> {
        try {
            val response = categoryApi.update("Bearer $accessToken",updateCategoryForm,id)
            if (response.isSuccessful){
                return RequestState.SUCCESS("Create category successfully")
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<UpdateCategoryResponse.Data>>(it).message
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

    override suspend fun deleteCategory(
        accessToken: String,
        id: Int
    ): RequestState<String> {
        try {
            val response = categoryApi.delete("Bearer $accessToken",id)
            if (response.isSuccessful){
                return RequestState.SUCCESS("Delete category successfully")
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                return RequestState.ERROR(it)
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