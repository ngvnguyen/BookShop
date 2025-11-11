package com.ptit.feature.form

import com.ptit.data.model.user.create.CreateUserForm
import com.ptit.data.model.user.fetch.UserResponseData
import com.ptit.data.model.user.update.UpdateUserForm
import com.ptit.feature.domain.Gender
import com.ptit.feature.viewmodel.getStatus
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val formatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("yyyy-MM-dd")
data class UserForm(
    val id:Int = 0,
    val name:String="",
    val phone:String?="",
    val email:String="",
    val password:String="",
    val roleId:Int = 1,
    val roleName:String="no role",
    val isActive:Boolean = true,
    val gender: Gender = Gender.MALE,
    val dateOfBirth: LocalDate? = null,
    val avatarUrl:String?=null,
    val page :Int=1,
    val maxPage :Int=1
)
fun UserForm.toUpdateUserForm()= UpdateUserForm(
    avatar = avatarUrl,
    dateOfBirth = dateOfBirth?.format(formatter),
    email = email,
    gender = gender.name,
    name = name,
    phone = phone,
    role = UpdateUserForm.Role(roleId),
    status = getStatus(isActive)
)

fun UserForm.toCreateUserForm() = CreateUserForm(
    avatar = avatarUrl,
    dateOfBirth = dateOfBirth?.format(formatter),
    email = email,
    gender = gender.name,
    name = name,
    password = password,
    phone = phone,
    role = CreateUserForm.Role(roleId),
    status = getStatus(isActive)
)

fun UserResponseData.toUserForm(page:Int,maxPage: Int) = UserForm(
    id =id,
    name = name,
    phone = phone,
    email = email,
    password = "",
    roleId = role?.id?:0,
    roleName = role?.name?:"no role",
    isActive = status == "ACTIVE",
    gender = Gender.valueOf(gender),
    dateOfBirth = dateOfBirth?.let {LocalDate.parse(it,formatter) },
    avatarUrl = avatarUrl,
    page = page,
    maxPage = maxPage
)