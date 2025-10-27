package com.ptit.data.model.auth.resetpassword

import kotlinx.serialization.Serializable

@Serializable
data class OTP(
    val otp :String
)
