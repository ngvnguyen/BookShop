package com.ptit.feature.form

import com.ptit.data.model.author.fetch.AuthorResponseData
import com.ptit.feature.domain.Gender


data class AuthorForm(
    val id:Int=0,
    val name: String = "",
    val biography:String="",
    val country:String="",
    val dateOfBirth:String="",
    val gender: Gender=Gender.MALE,
    val createdAt:String="",
    val createdBy:String="",
    val status:Status=Status.ACTIVE
){
    enum class Status{
        ACTIVE,INACTIVE
    }
}
fun AuthorResponseData.toAuthorForm() = AuthorForm(
    id = id,
    name = name,
    biography = biography,
    country = country,
    dateOfBirth = dateOfBirth?:"",
    gender = Gender.valueOf(gender),
    createdAt = createdAt,
    createdBy = createdBy,
    status = AuthorForm.Status.valueOf(status)
)