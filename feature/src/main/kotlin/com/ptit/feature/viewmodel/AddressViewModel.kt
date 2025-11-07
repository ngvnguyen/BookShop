package com.ptit.feature.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptit.data.RequestState
import com.ptit.data.repository.AddressRepository
import com.ptit.feature.util.SessionManager
import com.ptit.feature.form.AddressForm
import com.ptit.feature.form.toAddressForm
import com.ptit.feature.form.toAddressRequestForm
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AddressViewModel(
    private val addressRepository: AddressRepository,
    private val sessionManager: SessionManager
): ViewModel() {
    private val accessTokenFlow
        get() = sessionManager.accessToken
    private var accessToken = ""

    init{
        viewModelScope.launch {
            accessToken = accessTokenFlow.filterNotNull().first()
        }
    }
    private val refreshTrigger = MutableStateFlow(0)
    var isEdit by mutableStateOf(false)

    val allAddress : StateFlow<RequestState<List<AddressForm>>> = combine(accessTokenFlow,refreshTrigger){token,_ ->
        token
    }.flatMapLatest { token->
        flow {
            emit(RequestState.LOADING)
            if (token!=null){
                val response = addressRepository.getAllAddress(token)
                if (response.isSuccess()) emit(RequestState.SUCCESS(response.getSuccessData().map { it.toAddressForm() }))
                else emit(RequestState.ERROR(response.getErrorMessage()))
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.LOADING
    )
    var selectedAddressId by mutableIntStateOf(-1);private set
    val defaultId : StateFlow<Int?> = allAddress.map {addressState->
        Log.d("Default Id",addressState.isSuccess().toString())
        if (addressState.isSuccess()) addressState.getSuccessData().firstOrNull {it.isDefault}?.id
        else null
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    var addressForm by mutableStateOf(AddressForm())

    fun edit(){
        isEdit = true
    }
    fun create(){
        isEdit = false
    }

    fun selectAddressId(id:Int){
        selectedAddressId = if (id==defaultId.value) -1 else id
    }
    fun selectAddress(address: AddressForm){
        addressForm = address
    }

    fun resetAddressForm(){
        addressForm = AddressForm()
    }

    fun updateCity(city:String){
        addressForm = addressForm.copy(city=city)
    }
    fun updateDistrict(district:String){
        addressForm = addressForm.copy(district=district)
    }
    fun updateWard(ward:String){
        addressForm = addressForm.copy(ward=ward)
    }
    fun updateStreet(street:String){
        addressForm = addressForm.copy(street=street)
    }
    fun updateReceiverName(name:String){
        addressForm = addressForm.copy(receiverName=name)
    }
    fun updatePhone(phone:String){
        addressForm = addressForm.copy(phone=phone)
    }
    fun updateIsDefault(isDefault:Boolean){
        addressForm = addressForm.copy(isDefault=isDefault)
    }

    fun submitAddress(
        onSuccess:suspend ()->Unit,
        onError: suspend (String)->Unit
    ){
        viewModelScope.launch {
            try {
                if (selectedAddressId==-1) {
                    onSuccess()
                    return@launch
                }
                val response = addressRepository.updateAddress(
                    accessToken,
                    selectedAddressId,
                    allAddress.value.getSuccessData()
                        .first{it.id == selectedAddressId}
                        .toAddressRequestForm()
                        .copy(isDefault = true)
                )
                if (response.isSuccess()) onSuccess()
                else onError(response.getErrorMessage())
            }catch (e: Exception){
                onError("Unexpected error occurred")
            }
        }
    }

    fun updateAddress(
        onSuccess: suspend () -> Unit,
        onError:suspend (String) -> Unit
    ){
        viewModelScope.launch {
            val response = addressRepository.updateAddress(accessToken,addressForm.id,addressForm.toAddressRequestForm())
            if (response.isSuccess()) {
                onSuccess()
                refreshTrigger.value++
            }
            else onError(response.getErrorMessage())
        }
    }

    fun createAddress(
        onSuccess: suspend () -> Unit,
        onError:suspend (String) -> Unit
    ){
        viewModelScope.launch {
            val response = addressRepository.createNewAddress(accessToken,addressForm.toAddressRequestForm())
            if (response.isSuccess()) {
                onSuccess()
                refreshTrigger.value++
            }
            else onError(response.getErrorMessage())
        }
    }
}