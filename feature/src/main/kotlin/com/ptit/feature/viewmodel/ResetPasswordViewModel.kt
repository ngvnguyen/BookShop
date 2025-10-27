package com.ptit.feature.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptit.data.RequestState
import com.ptit.data.repository.AuthRepository
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val email = savedStateHandle.get<String>("email")?:"OOPS"

    var sendOTPState: RequestState<String> by mutableStateOf(RequestState.LOADING)
    var verifyOTPState: RequestState<String> by mutableStateOf(RequestState.IDLE)
    var changeNewPasswordState: RequestState<String> by mutableStateOf(RequestState.IDLE)
    var otp by mutableStateOf("")
        private set
    var newPassword by mutableStateOf("")
        private set
    var confirmNewPassword by mutableStateOf("")
        private set
    val isPasswordCorrect
        get() = newPassword.length>=6 && newPassword==confirmNewPassword
    init{
        viewModelScope.launch {
            sendOTPState = authRepository.sendOTP(email)
        }
    }


    fun verifyOTP(){
        if (otp.isDigitsOnly() && otp.length==6){
            viewModelScope.launch {
                verifyOTPState = RequestState.LOADING
                verifyOTPState = authRepository.verifyOTP(otp)
            }
        }
    }

    fun resetVerifyOTP(){
        verifyOTPState = RequestState.IDLE
    }

    fun changePassword(
        onSuccess:(String)->Unit,
        onError:(String)->Unit
    ){
        if (newPassword==confirmNewPassword){
            val resetToken = verifyOTPState.getSuccessData()
            viewModelScope.launch {
                changeNewPasswordState = RequestState.LOADING
                changeNewPasswordState = authRepository.resetPassword(resetToken,newPassword)

                if (changeNewPasswordState.isSuccess()) onSuccess(changeNewPasswordState.getSuccessData())
                else onError(changeNewPasswordState.getErrorMessage())
            }
        }
    }

    fun changeOTP(newOTP:String){
        if(newOTP.length<=6) otp = newOTP
    }

    fun changeNewPassword(newPass:String){
        newPassword = newPass
    }
    fun changeConfirmNewPassword(confirmPass:String){
        confirmNewPassword = confirmPass
    }


}