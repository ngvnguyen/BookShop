package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.permisson.create.CreatePermissionForm
import com.ptit.data.model.permisson.create.CreatePermissionResponse
import com.ptit.data.model.permisson.fetch.PermissionResponseData
import com.ptit.data.model.permisson.update.UpdatePermissionForm
import com.ptit.data.model.permisson.update.UpdatePermissionResponse

interface PermissionRepository {
    suspend fun getPermission(accessToken:String,id:Int): RequestState<PermissionResponseData>
    suspend fun getAllPermission(accessToken: String): RequestState<List<PermissionResponseData>>
    suspend fun createPermission(accessToken: String,createPermissionForm: CreatePermissionForm): RequestState<Int>
    suspend fun updatePermission(accessToken: String,id:Int,updatePermissionForm: UpdatePermissionForm): RequestState<Int>
    suspend fun deletePermission(accessToken: String,id:Int): RequestState<Int>
}