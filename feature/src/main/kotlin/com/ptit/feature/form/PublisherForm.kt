package com.ptit.feature.form

import com.ptit.data.model.publisher.fetch.PublisherResponseData

data class PublisherForm(
    val id:Int=0,
    val name:String="",
    val address:String="",
    val email:String="",
    val phone:String="",
    val status:Status=Status.ACTIVE,
    val createdBy:String="",
    val createdAt:String=""
){
    enum class Status{
        ACTIVE,INACTIVE
    }
}

fun PublisherResponseData.toPublisherForm() = PublisherForm(
    id = id,
    name = name,
    address = address,
    email = email,
    phone = phone,
    status = PublisherForm.Status.valueOf(status),
    createdBy = createdBy,
    createdAt = createdAt
)
