package com.ptit.feature.form

import com.ptit.data.model.role.create.CreateRoleForm
import com.ptit.data.model.role.fetch.RoleResponseData
import com.ptit.data.model.role.update.UpdateRoleForm
import com.ptit.feature.viewmodel.getStatus


data class RoleForm(
    val id: Int=0,
    val name:String = "",
    val description:String ="",
    val listPermission : Map<Int,String> = mapOf(),
    val status:RoleForm.Status = Status.ACTIVE,
    val createdAt: String?=null,
    val createdBy: String?=null
){
    enum class Status{
        ACTIVE, INACTIVE,DELETED;
        fun opposite():Status{
            return when (this){
                ACTIVE-> INACTIVE
                else -> ACTIVE
            }
        }
    }
}
fun RoleResponseData.toRoleForm() = RoleForm(
    id = id,
    name = name,
    description = description,
    listPermission = permissions.associate { p->
        p.id to p.name
    },
    status = RoleForm.Status.valueOf(status),
    createdAt = createdAt,
    createdBy = createdBy
)
fun RoleForm.toUpdateRoleForm() = UpdateRoleForm(
    description = description,
    name = name,
    permissions = listPermission.map { (id,_)->
        UpdateRoleForm.Permission(id)
    },
    status = status.name
)
fun RoleForm.toCreateRoleForm() = CreateRoleForm(
    description = description,
    name = name,
    permissions = listPermission.map { (id,_)->
        CreateRoleForm.Permission(id)
    },
    status = status.name
)