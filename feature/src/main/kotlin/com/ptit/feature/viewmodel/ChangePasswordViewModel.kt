package com.ptit.feature.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptit.data.model.auth.changepassword.ChangePasswordForm
import com.ptit.data.repository.AuthRepository
import com.ptit.feature.util.SessionManager
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
): ViewModel() {

    private val accessTokenFlow
        get() = sessionManager.accessToken
    private var accessToken = ""
    var changePasswordForm by mutableStateOf(ChangePasswordForm("","",""));private set
    val regex = Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$")

    val isValidPassword:Boolean
        get() = regex.matches(changePasswordForm.newPassword)
    val isPassMatchConfirmPass:Boolean
        get() = changePasswordForm.newPassword == changePasswordForm.confirmNewPassword

    init{
        viewModelScope.launch {
            accessToken = accessTokenFlow.filterNotNull().first()
        }
    }

    fun updateCurrentPassword(currentPassword: String) {
        changePasswordForm= changePasswordForm.copy(currentPassword = currentPassword)
    }
    fun updateNewPassword(newPassword: String) {
        changePasswordForm= changePasswordForm.copy(newPassword = newPassword)
    }
    fun updateConfirmNewPassword(confirmNewPassword: String) {
        changePasswordForm= changePasswordForm.copy(confirmNewPassword = confirmNewPassword)
    }


    fun changePassword(
        onSuccess:suspend (String)->Unit,
        onError:suspend (String)->Unit
    ){
        viewModelScope.launch {
            val response = authRepository.changePassword(accessToken,changePasswordForm)
            if (response.isSuccess()) onSuccess(response.getSuccessData())
            else onError(response.getErrorMessage())
        }
    }
}