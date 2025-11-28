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
        ACTIVE,INACTIVE,DELETED;
        fun isActive() = this == ACTIVE
    }

    enum class Method{
        GET,POST,PUT,DELETE,PATCH
    }
    enum class Module(
        val title:String,
        @DrawableRes val icon:Int?=null
    ){
        USERS("Manage User", Resources.Icon.Person),
        ROLES("Manage Role",Resources.Icon.Group),
        CATEGORIES("Manage Category",Resources.Icon.Category),
        AUTHORS("Manage Author",Resources.Icon.Author),
        PUBLISHERS("Manage Publisher",Resources.Icon.Domain),
        BOOKS("Manage Book",Resources.Icon.Book),
        PERMISSIONS("Manage Permission",Resources.Icon.Key),
        ORDERS("Manage Order",Resources.Icon.ShoppingCart),
        COUPONS("Manage Coupons",Resources.Icon.Voucher),
    }

    fun isUser() = this.module == Module.USERS
    fun isRole() = this.module == Module.ROLES
    fun isCategory() = this.module == Module.CATEGORIES
    fun isAuthor() = this.module == Module.AUTHORS
    fun isPublisher() = this.module == Module.PUBLISHERS
    fun isBook() = this.module == Module.BOOKS
    fun isPermission() = this.module == Module.PERMISSIONS
    fun isCoupon() = this.module == Module.COUPONS
    fun isOrder() = this.module == Module.ORDERS
}

fun PermissionResponseData.toPermissionData() =
    Permission.PermissionData(
        id = this.id,
        name = this.name,
        method = this.getMethod(),
        createdAt = this.createdAt,
        createdBy = this.createdBy,
        apiPath = this.apiPath,
        status = Permission.Status.valueOf(this.status)
    )
fun PermissionResponseData.getModule(): Permission.Module {
    try {
        val module = Permission.Module.valueOf(this.module)
        return module
    }catch (e: Exception){
        e.printStackTrace()
        println(module)
        return Permission.Module.BOOKS
    }
}
fun PermissionResponseData.getMethod(): Permission.Method {
    try {
        val method = Permission.Method.valueOf(this.method)
        return method
    }catch (e: Exception){
        e.printStackTrace()
        println(method)
        return Permission.Method.GET
    }
}
fun List<PermissionResponseData>.toPermissions(): List<Permission>{
    val map = this.distinct().groupBy { it.getModule() }
    return map.map{(module, listPermissionResponseData)->
        Permission(
            data = listPermissionResponseData.map { it.toPermissionData() },
            module = module
        )
    }
}
