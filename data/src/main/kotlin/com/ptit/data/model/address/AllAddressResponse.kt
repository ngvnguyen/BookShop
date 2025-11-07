package com.ptit.data.model.address


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllAddressResponse(
    @SerialName("addresses")
    val addresses: List<AddressData>,
    @SerialName("total")
    val total: Int
)