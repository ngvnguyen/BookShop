package com.ptit.data.model.auth.resetpassword

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordForm(
    @SerialName("reset_token")
    val resetToken:String,
    @SerialName("new_password")
    val newPassword:String,
    @SerialName("confirm_new_password")
    val confirmNewPassword:String
)
