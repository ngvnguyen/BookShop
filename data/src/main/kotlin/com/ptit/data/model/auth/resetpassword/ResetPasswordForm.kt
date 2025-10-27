package com.ptit.data.model.auth.resetpassword

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordForm(
    val resetToken:String,
    val newPassword:String,
    val confirmNewPassword:String
)
