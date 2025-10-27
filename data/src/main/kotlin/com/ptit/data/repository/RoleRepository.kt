package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.role.create.CreateRoleForm
import com.ptit.data.model.role.fetch.RoleResponseData
import com.ptit.data.model.role.update.UpdateRoleForm
import com.ptit.data.model.role.update.UpdateRoleResponse

interface RoleRepository {
    suspend fun getRole(accessToken:String, roleId: Int): RequestState<RoleResponseData>
    suspend fun getAllRole(accessToken: String): RequestState<List<RoleResponseData>>
    suspend fun createRole(accessToken: String,createRoleForm: CreateRoleForm): RequestState<Int>
    suspend fun updateRole(accessToken: String,id:Int,updateRoleForm: UpdateRoleForm): RequestState<Int>
    suspend fun deleteRole(accessToken: String,id:Int): RequestState<Unit>
}