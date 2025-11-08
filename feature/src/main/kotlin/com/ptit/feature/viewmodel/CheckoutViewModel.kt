package com.ptit.feature.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptit.data.RequestState
import com.ptit.data.repository.AddressRepository
import com.ptit.data.repository.CheckoutRepository
import com.ptit.feature.util.SessionManager
import com.ptit.feature.util.SharedState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModel(
    private val checkoutRepository: CheckoutRepository,
    private val addressRepository: AddressRepository,
    private val sharedState: SharedState,
    private val sessionManager: SessionManager
): ViewModel() {
    private val accessTokenFlow
        get() = sessionManager.accessToken
    private var accessToken = ""
    private val checkoutRequest get()= sharedState.checkoutRequest
    init{
        viewModelScope.launch {
            accessToken = accessTokenFlow.filterNotNull().first()
        }
    }

    val refreshTrigger = MutableStateFlow(0)
    val checkoutData = combine(accessTokenFlow,refreshTrigger) { token,_->token  }
        .flatMapLatest { token->
            flow {
                if (token!=null && checkoutRequest!=null){
                    val response = checkoutRepository.checkout(token,checkoutRequest!!)
                    emit(response)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )


}