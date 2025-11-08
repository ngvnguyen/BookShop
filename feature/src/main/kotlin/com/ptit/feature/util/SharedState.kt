package com.ptit.feature.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ptit.data.model.checkout.CheckoutRequest

class SharedState {
    var couponCode:String? by mutableStateOf(null);private set
    var addressId:Int by mutableIntStateOf(-1);private set
    var checkoutRequest : CheckoutRequest? by mutableStateOf(null)

    fun setCoupon(code:String?){
        couponCode = code
    }
    fun setAddress(id:Int){
        addressId = id
    }
}