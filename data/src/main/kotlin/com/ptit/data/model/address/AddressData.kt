package com.ptit.data.model.address


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressData(
    @SerialName("city")
    val city: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("district")
    val district: String,
    @SerialName("id")
    val id: Int,
    @SerialName("is_default")
    val isDefault: Boolean,
    @SerialName("phone")
    val phone: String,
    @SerialName("receiver_name")
    val receiverName: String,
    @SerialName("status")
    val status: String,
    @SerialName("street")
    val street: String,
    @SerialName("update_at")
    val updateAt: String?,
    @SerialName("ward")
    val ward: String
)