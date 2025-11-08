package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.checkout.CheckoutData
import com.ptit.data.model.checkout.CheckoutRequest

interface CheckoutRepository {
    suspend fun checkout(token:String,checkoutRequest: CheckoutRequest): RequestState<CheckoutData>
}