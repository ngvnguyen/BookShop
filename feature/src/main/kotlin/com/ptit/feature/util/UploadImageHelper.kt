package com.ptit.feature.util

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.ptit.data.RequestState
import com.ptit.data.model.file.UploadFileData
import com.ptit.data.repository.FileRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadImageHelper(private val fileRepository: FileRepository) {
    suspend fun uploadImage(accessToken:String,uri: Uri,contentResolver: ContentResolver): RequestState<UploadFileData>{
        try{
            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream == null){
                return RequestState.ERROR("Cannot open input stream from URI")
            }
            val imageBytes = inputStream.readBytes()
            inputStream.close()

            val fileName = getFileNameFromUri(uri,contentResolver)
            val mimeType = contentResolver.getType(uri)?:"image/*"

            val fileBody = imageBytes.toRequestBody(contentType = mimeType.toMediaTypeOrNull())

            val imagePart = MultipartBody.Part.createFormData(
                name = "file",
                filename = fileName,
                body = fileBody
            )
            return fileRepository.uploadFile(accessToken,imagePart)
        }catch (e: Exception){
            return RequestState.ERROR("Error: ${e.message}")
        }
    }

    fun getFileNameFromUri(uri: Uri,contentResolver: ContentResolver):String{
        var fileName = "image.jpg"

        contentResolver.query(uri,null,null,null)?.use{cursor->
            if (cursor.moveToFirst()){
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1){
                    fileName = cursor.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}