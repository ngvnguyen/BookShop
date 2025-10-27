package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.FileApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.file.UploadFileData
import com.ptit.data.repository.FileRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.SocketTimeoutException

class FileRepositoryImpl(private val fileApi: FileApi): FileRepository {
    override suspend fun uploadFile(
        accessToken: String,
        filePart: MultipartBody.Part
    ): RequestState<UploadFileData> {
        try {
            val folder = "folder".toRequestBody(contentType = "text/plain".toMediaType())
            val response = fileApi.uploadFile(
                "Bearer $accessToken",
                filePart,
                folder
            )

            if (response.isSuccessful){
                val uploadFileData = response.body()?.data
                uploadFileData?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<UploadFileData>>(body).message
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