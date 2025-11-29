package com.ptit.feature.form

import com.ptit.data.model.book.create.CreateBookForm
import com.ptit.data.model.book.fetch.BookResponseData
import com.ptit.data.model.book.update.UpdateBookForm
import com.ptit.feature.viewmodel.getStatus

data class BookForm(
    val id:Int = 0,
    val name:String="",
    val description:String="",
    val author: Author?=null,
    val publisher: Publisher?=null,
    val category: Category?=null,
    val price: Double = 0.0,
    val image:String?=null,
    val quantity:Int=0,
    val discount:Double=0.0,
    val language:String="",
    val createdAt:String?=null,
    val updatedAt:String?=null,
    val status: Status= Status.AVAILABLE,
    val page:Int = 0,
    val maxPage:Int = 0
){
    data class Author(
        val id: Int,
        val name: String
    )

    data class Category(
        val id: Int,
        val name: String
    )

    data class Publisher(
        val id: Int,
        val name: String
    )
    enum class Status(val title: String){
        AVAILABLE("AVAILABLE"),
        OUT_OF_STOCK("OUT OF STOCK"),
        DELETED("DELETED");
        fun isAvailable() = this == AVAILABLE
    }
}

fun BookResponseData.toBookForm(page:Int,maxPage: Int) = BookForm(
    id = id,
    name = name,
    description = description,
    author = BookForm.Author(author.id, author.name),
    publisher = BookForm.Publisher(publisher.id,publisher.name),
    category = BookForm.Category(category.id,category.name),
    price = price,
    image = image,
    quantity = quantity,
    discount = discount,
    language = language,
    createdAt = createdAt,
    updatedAt = updatedAt,
    status = BookForm.Status.valueOf(status),
    page = page,
    maxPage = maxPage
)

fun BookForm.toCreateBookForm(): CreateBookForm = try {
    CreateBookForm(
        author = CreateBookForm.Author(author!!.id),
        category = CreateBookForm.Category(category!!.id),
        description = description,
        discount = discount,
        image = image!!,
        language = language,
        name = name,
        price = price,
        publisher = CreateBookForm.Publisher(publisher!!.id),
        quantity = quantity
    )
}catch (e: Exception){
    e.printStackTrace()
    throw Exception("Author, Category, Publisher, Image must not be null")
}

fun BookForm.toUpdateBookForm(): UpdateBookForm = try {
    UpdateBookForm(
        author = UpdateBookForm.Author(author!!.id),
        category = UpdateBookForm.Category(category!!.id),
        description = description,
        discount = discount,
        image = image!!,
        language = language,
        name = name,
        price = price,
        publisher = UpdateBookForm.Publisher(publisher!!.id),
        quantity = quantity,
        status = status.name
    )
}catch (e: Exception){
    e.printStackTrace()
    throw Exception("Author, Category, Publisher, Image must not be null")
}