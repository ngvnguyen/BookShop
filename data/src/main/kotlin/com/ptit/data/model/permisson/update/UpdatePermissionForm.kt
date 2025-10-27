package com.ptit.data.model.permisson.update


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePermissionForm(
    @SerialName("api_path")
    val apiPath: String,
    @SerialName("method")
    val method: String,
    @SerialName("module")
    val module: String,
    @SerialName("name")
    val name: String,
    @SerialName("status")
    val status: String
)