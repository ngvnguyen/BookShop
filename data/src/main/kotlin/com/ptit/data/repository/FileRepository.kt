package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.file.UploadFileData
import okhttp3.MultipartBody
import java.io.File

interface FileRepository {
    suspend fun uploadFile(accessToken:String, filePart: MultipartBody.Part): RequestState<UploadFileData>
}