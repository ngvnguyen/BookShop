package com.ptit.feature.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptit.data.RequestState
import com.ptit.data.repository.OrderRepository
import com.ptit.feature.domain.OrderEnum
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
class OrderViewModel(
    private val orderRepository: OrderRepository,
    private val sessionManager: SessionManager
): ViewModel() {
    private val accessTokenFlow
        get() = sessionManager.accessToken
    private var accessToken = ""
    private val refreshTrigger = MutableStateFlow(0)
    val filterOrderIndex = MutableStateFlow(0)

    init{
        viewModelScope.launch {
            accessToken = accessTokenFlow.filterNotNull().first()
        }
    }
    private val orders = combine(accessTokenFlow,refreshTrigger) { token, _->token  }
        .flatMapLatest {token->
            flow {
                emit(RequestState.LOADING)
                if (token!=null){
                    val response = orderRepository.getOrders(token)
                    if (response.isSuccess()) emit(RequestState.SUCCESS(response.getSuccessData().orders))
                    else emit(RequestState.ERROR(response.getErrorMessage()))
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )
    val ordersFilter = combine(orders,filterOrderIndex) { allOrder, index->
        index to allOrder
    }.flatMapLatest { (index,allOrder)->
        flow {
            when {
                allOrder.isSuccess() -> {
                    val data = allOrder.getSuccessData().filter { it.status == OrderEnum.getOrderTabByIndex(index).name }
                    emit(RequestState.SUCCESS(data))
                }
                else -> emit(allOrder)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.LOADING
    )


    fun setFilterOrderIndex(index:Int){
        filterOrderIndex.value = index
    }

}