package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.author.fetch.AuthorResponseData
import com.ptit.data.model.category.fetch.CategoryResponseData

interface AuthorRepository {
    suspend fun getAllAuthor(accessToken:String): RequestState<List<AuthorResponseData>>
}