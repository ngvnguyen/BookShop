package com.ptit.data.model.permisson.create


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class CreatePermissionResponse {
    @Serializable
    data class Data(
        @SerialName("api_path")
        val apiPath: String,
        @SerialName("created_at")
        val createdAt: String,
        @SerialName("created_by")
        val createdBy: String,
        @SerialName("id")
        val id: Int,
        @SerialName("method")
        val method: String,
        @SerialName("module")
        val module: String,
        @SerialName("name")
        val name: String,
        @SerialName("status")
        val status: String
    )
}