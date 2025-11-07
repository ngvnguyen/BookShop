package com.ptit.feature.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptit.data.RequestState
import com.ptit.data.model.cart.AddToCartForm
import com.ptit.data.repository.BookRepository
import com.ptit.data.repository.CartRepository
import com.ptit.feature.util.SessionManager
import com.ptit.feature.form.BookForm
import com.ptit.feature.form.toBookForm
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val cartRepository: CartRepository,
    private val sessionManager: SessionManager
): ViewModel(){
    val bookId = savedStateHandle.get<Int>("bookId")
    var book: RequestState<BookForm> by mutableStateOf(RequestState.LOADING)
    private val accessTokenFlow
        get()= sessionManager.accessToken
    private var accessToken = ""
    init {
        viewModelScope.launch {
            accessTokenFlow.filterNotNull().first().let { token->
                accessToken = token
                bookId?.let { id->
                    val bookResponse = bookRepository.getBookById(token,id)
                    if (bookResponse.isSuccess()){
                        val data = bookResponse.getSuccessData()
                        book = RequestState.SUCCESS(data.toBookForm(0,0))
                    }else book = RequestState.ERROR(bookResponse.getErrorMessage())
                }
            }

        }
    }

    fun addToCart(
        onSuccess:suspend ()->Unit,
        onError: suspend (String)->Unit
    ){
        viewModelScope.launch {
            val response = bookId?.let{
                cartRepository.addItemToCart(
                    accessToken = accessToken,
                    addToCartForm = AddToCartForm(
                        productId = it,
                        quantity = 1
                    )
                )
            }?: RequestState.ERROR("Invalid book id")
            if (response.isSuccess()) onSuccess()
            else onError(response.getErrorMessage())
        }
    }


}