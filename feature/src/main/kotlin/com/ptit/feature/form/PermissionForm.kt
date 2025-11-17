package com.ptit.feature.form

import com.ptit.data.model.permisson.create.CreatePermissionForm
import com.ptit.data.model.permisson.update.UpdatePermissionForm
import com.ptit.feature.permission.Permission


data class PermissionForm(
    val name:String="",
    val isActive:Boolean=true,
    val apiPath:String="",
    val method: Permission.Method = Permission.Method.GET,
    val module: Permission.Module = Permission.Module.USERS,
    val id :Int?=null,
    val createdAt:String?=null,
    val createdBy:String?=null
)

fun Permission.PermissionData.toPermissionForm(module: Permission.Module) = PermissionForm(
    name = this.name,
    isActive = this.status == Permission.Status.ACTIVE,
    apiPath = this.apiPath,
    method = this.method,
    module = module,
    id = this.id,
    createdAt = this.createdAt,
    createdBy = this.createdBy
)
fun PermissionForm.toUpdatePermissionForm() = UpdatePermissionForm(
    name = this.name,
    apiPath = this.apiPath,
    method = this.method.name,
    module = this.module.name,
    status = if (this.isActive) Permission.Status.ACTIVE.name else Permission.Status.INACTIVE.name
)
fun PermissionForm.toCreatePermissionForm() = CreatePermissionForm(
    name = this.name,
    apiPath = this.apiPath,
    method = this.method.name,
    module = this.module.name,
)