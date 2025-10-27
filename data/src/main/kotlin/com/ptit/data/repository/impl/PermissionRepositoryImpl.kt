package com.ptit.data.repository.impl
import com.ptit.data.RequestState
import com.ptit.data.api.PermissionApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.permisson.create.CreatePermissionForm
import com.ptit.data.model.permisson.create.CreatePermissionResponse
import com.ptit.data.model.permisson.fetch.FetchAllPermissionResponse
import com.ptit.data.model.permisson.fetch.PermissionResponseData
import com.ptit.data.model.permisson.update.UpdatePermissionForm
import com.ptit.data.model.permisson.update.UpdatePermissionResponse
import com.ptit.data.repository.PermissionRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class PermissionRepositoryImpl(private val permissionApi: PermissionApi) : PermissionRepository {
    override suspend fun getPermission(
        accessToken: String,
        id: Int
    ): RequestState<PermissionResponseData> {
        try {
            val response = permissionApi.getPermissionById("Bearer $accessToken",id)
            if (response.isSuccessful){
                val permissionResponseData = response.body()?.data
                permissionResponseData?.let{return RequestState.SUCCESS(it)}
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<PermissionResponseData>>(body).message
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

    override suspend fun getAllPermission(accessToken: String): RequestState<List<PermissionResponseData>> {
        try {
            val response = permissionApi.getAllPermission("Bearer $accessToken")
            if (response.isSuccessful){
                val listPermission = response.body()?.data?.permissions
                listPermission?.let{return RequestState.SUCCESS(it)}
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<FetchAllPermissionResponse.Data>>(body).message
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

    override suspend fun createPermission(
        accessToken: String,
        createPermissionForm: CreatePermissionForm
    ): RequestState<Int> {
        try{
            val response = permissionApi.create("Bearer $accessToken",createPermissionForm)
            if (response.isSuccessful){
                val createPermissionData = response.body()?.data
                createPermissionData?.let { return RequestState.SUCCESS(it.id) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<CreatePermissionResponse.Data>>(body)
                    .message
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

    override suspend fun updatePermission(
        accessToken: String,
        id:Int,
        updatePermissionForm: UpdatePermissionForm
    ): RequestState<Int> {
        try{
            val response = permissionApi.update("Bearer $accessToken",updatePermissionForm,id)
            if (response.isSuccessful){
                val updatePermissionData = response.body()?.data
                updatePermissionData?.let { return RequestState.SUCCESS(it.id) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<UpdatePermissionResponse.Data>>(body)
                    .message
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

    override suspend fun deletePermission(
        accessToken: String,
        id: Int
    ): RequestState<Int> {
        try {
            val response = permissionApi.delete("Bearer $accessToken",id)
            if (response.isSuccessful){
                return RequestState.SUCCESS(id)
            }else{
                val errBody = response.errorBody()?.string()
                errBody?.let { body->
                    return RequestState.ERROR(body)
                }
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