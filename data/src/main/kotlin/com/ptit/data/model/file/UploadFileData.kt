package com.ptit.data.model.file


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadFileData(
    @SerialName("file_name")
    val fileName: String,
    @SerialName("url_file")
    val urlFile:String,
    @SerialName("upload_at")
    val uploadAt: String
)