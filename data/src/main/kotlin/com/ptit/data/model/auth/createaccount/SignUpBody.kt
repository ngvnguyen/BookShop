package com.ptit.data.model.auth.createaccount

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpBody(
    val name:String,
    val email:String,
    val phone:String,
    val password:String,
    @SerialName("confirm_password")
    val confirmPassword:String
)