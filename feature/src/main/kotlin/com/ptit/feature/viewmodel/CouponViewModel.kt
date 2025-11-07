package com.ptit.feature.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptit.data.RequestState
import com.ptit.data.model.coupon.CouponData
import com.ptit.data.repository.CouponRepository
import com.ptit.feature.util.SessionManager
import com.ptit.feature.util.SharedState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)

class CouponViewModel(
    private val couponRepository: CouponRepository,
    private val sessionManager: SessionManager,
    private val sharedState: SharedState
): ViewModel() {
    private val accessTokenFlow
        get() = sessionManager.accessToken
    private var accessToken = ""
    var filterCode by mutableStateOf("")
    private val code = MutableStateFlow("")

    init{
        viewModelScope.launch {
            accessToken = accessTokenFlow.filterNotNull().first()
        }
    }
    private val refreshTrigger = MutableStateFlow(0)
    private val allCoupons : StateFlow<RequestState<List<CouponData>>> = combine(accessTokenFlow,refreshTrigger) { token, _->token  }
        .flatMapLatest {token->
            flow {
                emit(RequestState.LOADING)
                if (token!=null){
                    val response = couponRepository.getAllCoupons(token)
                    emit(response)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )
    val filterCoupons: StateFlow<RequestState<List<CouponData>>> = combine(allCoupons,code) { coupons, code->code to coupons }
        .flatMapLatest {(code,couponsState)->
            flow{
                when{
                    !couponsState.isSuccess()->emit(couponsState)
                    code.isBlank() -> emit(couponsState)
                    else->{
                        val coupons = couponsState.getSuccessData()
                        val filtered = coupons.filter { it.code.lowercase().contains(code.lowercase()) }
                        emit(RequestState.SUCCESS(filtered))
                    }
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )

    fun setFilterCde(code:String){
        filterCode = code
    }
    fun filter(){
        code.value = filterCode
    }
    fun refresh(){
        refreshTrigger.value++
    }
    fun selectCoupon(coupon: CouponData?){
        sharedState.setCoupon(coupon?.code)
    }
}