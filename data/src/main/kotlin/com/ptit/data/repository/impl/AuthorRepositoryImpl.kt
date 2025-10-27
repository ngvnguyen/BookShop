package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.AuthorApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.author.fetch.AuthorResponseData
import com.ptit.data.model.author.fetch.FetchAllAuthorResponse
import com.ptit.data.model.category.fetch.CategoryResponseData
import com.ptit.data.repository.AuthorRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class AuthorRepositoryImpl(private val authorApi: AuthorApi): AuthorRepository {
    override suspend fun getAllAuthor(accessToken: String): RequestState<List<AuthorResponseData>> {
        try {
            val response = authorApi.getAllAuthor("Bearer $accessToken")
            if (response.isSuccessful){
                val allAuthor = response.body()?.data
                allAuthor?.let{return RequestState.SUCCESS(it.authors) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<FetchAllAuthorResponse.Data>>(it).message
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