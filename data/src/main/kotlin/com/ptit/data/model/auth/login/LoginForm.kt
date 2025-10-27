package com.ptit.data.model.auth.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginForm(
    val username:String,
    val password:String
)
