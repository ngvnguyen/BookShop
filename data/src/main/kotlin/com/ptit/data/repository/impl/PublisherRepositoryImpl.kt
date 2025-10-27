package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.PublisherApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.author.fetch.FetchAllAuthorResponse
import com.ptit.data.model.publisher.fetch.FetchAllPublisherResponse
import com.ptit.data.model.publisher.fetch.PublisherResponseData
import com.ptit.data.repository.PublisherRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class PublisherRepositoryImpl(private val publisherApi: PublisherApi): PublisherRepository {
    override suspend fun getAllPublisher(accessToken: String): RequestState<List<PublisherResponseData>> {
        try {
            val response = publisherApi.getAllPublisher("Bearer $accessToken")
            if (response.isSuccessful){
                val allAuthor = response.body()?.data
                allAuthor?.let{return RequestState.SUCCESS(it.publishers) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<FetchAllPublisherResponse.Data>>(it).message
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