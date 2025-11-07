package com.ptit.data.model.address


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressRequestForm(
    @SerialName("city")
    val city: String,
    @SerialName("district")
    val district: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("receiver_name")
    val receiverName: String,
    @SerialName("street")
    val street: String,
    @SerialName("ward")
    val ward: String,
    @SerialName("is_default")
    val isDefault: Boolean
)