package com.ptit.feature.navigation

import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
sealed class Screen(){
    @Serializable
    object Auth: Screen()
    @Serializable
    object HomeGraph:Screen(){
        @Serializable
        object Home: Screen()
        @Serializable
        object Book: Screen()
        @Serializable
        object Cart: Screen()
    }
    @Serializable
    object Profile:Screen()
    @Serializable
    object ForgotPassword:Screen()
    @Serializable
    data class ResetPassword(val email:String): Screen()


    @Serializable
    object Admin: Screen()
    @Serializable
    object ManageUser: Screen(){
        @Serializable
        object CreateUser: Screen()
    }
    @Serializable
    object ManagePermission: Screen()
    @Serializable
    object ManageRole: Screen()
    @Serializable
    object ManageAuthor: Screen()
    @Serializable
    object ManageCategory: Screen()
    @Serializable
    object ManageBook: Screen()
    @Serializable
    object ManageCart: Screen()
    @Serializable
    object ManagePublisher: Screen()

}