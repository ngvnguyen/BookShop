package com.ptit.feature.form

import com.ptit.data.model.address.AddressData
import com.ptit.data.model.address.AddressRequestForm

data class AddressForm(
    val id:Int=0,
    val city:String="",
    val district:String="",
    val phone:String="",
    val receiverName:String="",
    val street:String="",
    val ward:String="",
    val isDefault:Boolean=false
)
fun AddressForm.toAddressRequestForm()= AddressRequestForm(
    city = city,
    district = district,
    phone = phone,
    receiverName = receiverName,
    street = street,
    ward = ward,
    isDefault = isDefault
)
fun AddressData.toAddressForm()= AddressForm(
    id = id,
    city = city,
    district = district,
    phone = phone,
    receiverName = receiverName,
    street = street,
    ward = ward,
    isDefault = isDefault
)
