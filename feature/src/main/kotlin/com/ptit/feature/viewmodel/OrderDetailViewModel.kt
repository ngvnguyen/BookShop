package com.ptit.feature.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptit.data.RequestState
import com.ptit.data.model.order.UpdateOrderStatusRequest
import com.ptit.data.repository.OrderRepository
import com.ptit.feature.util.SessionManager
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
class OrderDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val orderRepository: OrderRepository,
    private val sessionManager: SessionManager
): ViewModel() {
    private val orderId = savedStateHandle.getStateFlow("id",-1)
    private val accessTokenFlow
        get() = sessionManager.accessToken
    private var accessToken = ""
    private val refreshTrigger = MutableStateFlow(0)

    init{
        viewModelScope.launch {
            accessToken = accessTokenFlow.filterNotNull().first()
        }
    }
    val order = combine(accessTokenFlow,orderId,refreshTrigger) { token, id, trigger->token to id  }
        .flatMapLatest { (token,id)->
            flow {
                emit(RequestState.LOADING)
                if (token!=null && id!=-1){
                    val response = orderRepository.getOrderById(token,id)
                    emit(response)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )

    fun cancelOrder(
        onSuccess:suspend ()->Unit,
        onError:suspend (String)->Unit
    ){
        viewModelScope.launch {
            val data = order.value.getSuccessData()
            val response = orderRepository.updateOrderStatus(accessToken,data.id, UpdateOrderStatusRequest(
                UpdateOrderStatusRequest.OrderStatus.CANCELLED))
            if (response.isSuccess()) onSuccess()
            else onError(response.getErrorMessage())
        }
    }
}