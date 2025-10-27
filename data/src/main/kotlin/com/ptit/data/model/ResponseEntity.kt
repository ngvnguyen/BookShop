package com.ptit.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseEntity<T>(
    val data:T?,
    val error:String?=null,
    val message:String,
    @SerialName("status_code")
    val statusCode: Int
)