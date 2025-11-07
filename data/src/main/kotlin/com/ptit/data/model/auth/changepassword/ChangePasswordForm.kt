package com.ptit.data.model.auth.changepassword


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordForm(
    @SerialName("confirm_new_password")
    val confirmNewPassword: String,
    @SerialName("current_password")
    val currentPassword: String,
    @SerialName("new_password")
    val newPassword: String
)