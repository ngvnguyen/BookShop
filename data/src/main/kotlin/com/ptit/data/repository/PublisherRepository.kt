package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.publisher.fetch.PublisherResponseData

interface PublisherRepository {
    suspend fun getAllPublisher(accessToken: String): RequestState<List<PublisherResponseData>>

}