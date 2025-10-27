package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.user.create.CreateUserForm
import com.ptit.data.model.user.fetch.FetchUserPagedResponse
import com.ptit.data.model.user.fetch.UserResponseData
import com.ptit.data.model.user.update.UpdateProfileForm
import com.ptit.data.model.user.update.UpdateUserForm

interface UserRepository {
    suspend fun createUser(accessToken:String,createUserForm: CreateUserForm): RequestState<Int>
    suspend fun updateUser(accessToken:String,id:Int, updateUserForm: UpdateUserForm): RequestState<Int>
    suspend fun updateProfile(accessToken:String,id:Int, updateProfileForm: UpdateProfileForm): RequestState<Int>
    suspend fun deleteUser(accessToken:String,id:Int): RequestState<Int>
    suspend fun getUserById(accessToken:String,id:Int): RequestState<UserResponseData>
    suspend fun getUserPaged(accessToken:String,page:Int,userName:String?): RequestState<FetchUserPagedResponse.Data>
}