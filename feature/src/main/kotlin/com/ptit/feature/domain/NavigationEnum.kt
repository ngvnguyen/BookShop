package com.ptit.feature.domain

import androidx.annotation.DrawableRes
import com.ptit.feature.navigation.Screen
import com.ptit.shared.Resources

enum class NavigationEnum(
    val id:Int,
    val title:String,
    @DrawableRes val icon:Int,
    val screen: Screen
) {
    Home(1,"Home", Resources.Icon.Home,Screen.HomeGraph.Home),
    Book(2,"Book", Resources.Icon.Book,Screen.HomeGraph.Book),
    Cart(3,"Cart", Resources.Icon.ShoppingCart,Screen.HomeGraph.Cart),

}