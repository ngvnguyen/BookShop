package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.RoleApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.role.create.CreateRoleForm
import com.ptit.data.model.role.create.CreateRoleResponse
import com.ptit.data.model.role.fetch.FetchAllRoleResponse
import com.ptit.data.model.role.fetch.RoleResponseData
import com.ptit.data.model.role.update.UpdateRoleForm
import com.ptit.data.model.role.update.UpdateRoleResponse
import com.ptit.data.model.user.create.CreateUserResponse
import com.ptit.data.repository.RoleRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class RoleRepositoryImpl(private val roleApi: RoleApi): RoleRepository {
    override suspend fun getRole(accessToken:String, roleId: Int)
    : RequestState<RoleResponseData> {
        try {
            val response = roleApi.getRoleById("Bearer $accessToken",roleId)
            if (response.isSuccessful){
                val roleResponse = response.body()
                roleResponse?.data?.let{return RequestState.SUCCESS(it)}
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<RoleResponseData>>(body).message
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

    override suspend fun getAllRole(accessToken: String): RequestState<List<RoleResponseData>> {
        try {
            val response = roleApi.getAllRole("Bearer $accessToken")
            if (response.isSuccessful){
                val roleResponse = response.body()?.data
                roleResponse?.let {  return RequestState.SUCCESS(it.roles) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<FetchAllRoleResponse.Data>>(body).message
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

    override suspend fun createRole(
        accessToken: String,
        createRoleForm: CreateRoleForm
    ): RequestState<Int> {
        try {
            val response = roleApi.create(
                token = "Bearer $accessToken",
                body = createRoleForm
            )
            if (response.isSuccessful){
                val roleResponse = response.body()?.data
                roleResponse?.let {  return RequestState.SUCCESS(it.id) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<CreateRoleResponse.Data>>(body).message
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

    override suspend fun updateRole(
        accessToken: String,
        id: Int,
        updateRoleForm: UpdateRoleForm
    ): RequestState<Int> {
        try {
            val response = roleApi.update(
                token = "Bearer $accessToken",
                body = updateRoleForm,
                id = id
            )
            if (response.isSuccessful){
                val roleResponse = response.body()?.data
                roleResponse?.let {  return RequestState.SUCCESS(it.id) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let { body->
                val errMsg = Json.decodeFromString<ResponseEntity<UpdateRoleResponse.Data>>(body).message
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

    override suspend fun deleteRole(
        accessToken: String,
        id: Int
    ): RequestState<Unit> {
        try {
            val response = roleApi.delete(
                token = "Bearer $accessToken",
                id = id
            )
            if (response.isSuccessful){
                return RequestState.SUCCESS(Unit)
            }
            val errBody = response.errorBody()?.string()
            errBody?.let{return RequestState.ERROR(it)}
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }
}