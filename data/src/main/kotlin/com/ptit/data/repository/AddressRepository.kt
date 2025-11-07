package com.ptit.data.repository

import com.ptit.data.RequestState
import com.ptit.data.model.address.AddressData
import com.ptit.data.model.address.AddressRequestForm

interface AddressRepository {
    suspend fun createNewAddress(accessToken:String,createAddressRequest: AddressRequestForm): RequestState<String>
    suspend fun updateAddress(accessToken: String,id:Int,updateAddressRequest: AddressRequestForm): RequestState<String>
    suspend fun deleteAddress(accessToken: String,id:Int): RequestState<String>
    suspend fun getAllAddress(accessToken: String): RequestState<List<AddressData>>
    suspend fun getAddressById(accessToken: String,id:Int): RequestState<AddressData>
    suspend fun getDefaultAddress(accessToken: String): RequestState<AddressData>
}