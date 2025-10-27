package com.ptit.data.model.permisson.create


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePermissionForm(
    @SerialName("api_path")
    val apiPath: String,
    @SerialName("method")
    val method: String,
    @SerialName("module")
    val module: String,
    @SerialName("name")
    val name: String
)