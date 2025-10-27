package com.ptit.feature.domain

enum class DrawerState {
    Opened,Closed
}
fun DrawerState.isOpened() = this == DrawerState.Opened

fun DrawerState.opposite() = if (isOpened()) DrawerState.Closed
    else DrawerState.Opened