package com.ptit.feature.permission

import android.R
import androidx.annotation.DrawableRes
import com.ptit.data.model.permisson.fetch.FetchAllPermissionResponse
import com.ptit.data.model.permisson.fetch.PermissionResponseData
import com.ptit.shared.Resources



data class Permission(
    val data: List<PermissionData>,
    val module: Module
) {

    data class PermissionData(
        val id:Int,
        val name: String,
        val method: Method,
        val createdAt:String?=null,
        val createdBy:String?=null,
        val apiPath:String,
        val status: Status
    ){
        fun isGet() = this.method == Method.GET
        fun isPost() = this.method == Method.POST
        fun isPut() = this.method == Method.PUT
        fun isDelete() = this.method == Method.DELETE
    }
    enum class Status{
        ACTIVE,INACTIVE;
        fun isActive() = this == ACTIVE
    }

    enum class Method{
        GET,POST,PUT,DELETE
    }
    enum class Module(
        val title:String,
        @DrawableRes val icon:Int?=null
    ){
        User("Manage User", Resources.Icon.Person),
        Role("Manage Role",Resources.Icon.Group),
        Category("Manage Category",Resources.Icon.Category),
        Author("Manage Author",Resources.Icon.Author),
        Publisher("Manage Publisher",Resources.Icon.Domain),
        Book("Manage Book",Resources.Icon.Book),
        Cart("Manage Cart",Resources.Icon.ShoppingCart),
        Permission("Manage Permission",Resources.Icon.Key)
    }

    fun isUser() = this.module == Module.User
    fun isRole() = this.module == Module.Role
    fun isCategory() = this.module == Module.Category
    fun isAuthor() = this.module == Module.Author
    fun isPublisher() = this.module == Module.Publisher
    fun isBook() = this.module == Module.Book
    fun isCart() = this.module == Module.Cart
    fun isPermission() = this.module == Module.Permission
}

fun PermissionResponseData.toPermissionData() =
    Permission.PermissionData(
        id = this.id,
        name = this.name,
        method = Permission.Method.valueOf(this.method),
        createdAt = this.createdAt,
        createdBy = this.createdBy,
        apiPath = this.apiPath,
        status = Permission.Status.valueOf(this.status)
    )
fun PermissionResponseData.getModule() = Permission.Module.valueOf(this.module)
fun List<PermissionResponseData>.toPermissions(): List<Permission>{
    val map = this.distinct().groupBy { it.getModule() }
    return map.map{(module, listPermissionResponseData)->
        Permission(
            data = listPermissionResponseData.map { it.toPermissionData() },
            module = module
        )
    }
}
fun List<Permission>.addPermission(
    newPermissionData: Permission.PermissionData,
    module: Permission.Module)
: List<Permission> {
    val existingPermission = this.find { it.module == module}
    return if (existingPermission!=null){
        val updatedData = existingPermission.data.filterNot { it.id == newPermissionData.id }+
                newPermissionData
        this.map {
            if (it.module!=module) it
            else it.copy(data = updatedData)
        }

    }else
        this+
            Permission(
                data = listOf(newPermissionData),
                module = module
            )
}
fun List<Permission>.deletePermission(
    id:Int
): List<Permission>{
    return this.map { permission->
        if (permission.data.any{it.id == id}){
            permission.copy(
                data = permission.data.filterNot { it.id == id }
            )
        }else permission
    }
}
