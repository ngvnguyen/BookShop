package com.ptit.feature.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SharedState {
    var couponCode:String? by mutableStateOf(null);private set
    var addressId:Int by mutableIntStateOf(-1);private set

    fun setCoupon(code:String?){
        couponCode = code
    }
    fun setAddress(id:Int){
        addressId = id
    }
}