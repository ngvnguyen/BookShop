package com.ptit.feature.domain

import androidx.annotation.DrawableRes
import com.ptit.feature.navigation.Screen
import com.ptit.shared.Resources

enum class NavigationEnum(
    val title:String,
    @DrawableRes val icon:Int,
    val screen: Screen
) {
    Home("Home", Resources.Icon.Home,Screen.HomeGraph.Home),
    Book("Book", Resources.Icon.Book,Screen.HomeGraph.Book),
    Cart("Cart", Resources.Icon.ShoppingCart,Screen.HomeGraph.Cart),

}