package com.ptit.feature.domain

import androidx.annotation.DrawableRes
import com.ptit.shared.Resources

enum class CustomDrawerItem(
    val title:String,
    @DrawableRes val icon: Int
){
    Profile(
        "Profile",
        Resources.Icon.Person
    ),

    ChangePassword(
        "Change Password",
        Resources.Icon.Key
    ),

    Logout(
        "Log out",
        Resources.Icon.SignOut
    ),

    Admin(
        "Admin",
        Resources.Icon.Unlock
    )
}