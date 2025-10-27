package com.ptit.feature.viewmodel

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import com.ptit.data.RequestState
import com.ptit.feature.SessionManager
import com.ptit.data.model.auth.login.LoginForm
import com.ptit.data.model.auth.createaccount.SignUpBody
import com.ptit.data.model.auth.login.LoginResponse
import com.ptit.data.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlin.math.log


enum class AuthTarget{
    Login, SignUp
}
fun AuthTarget.opposite() = if (this == AuthTarget.Login) AuthTarget.SignUp else AuthTarget.Login
fun AuthTarget.isLogin() = this == AuthTarget.Login
fun AuthTarget.isSignUp() = this == AuthTarget.SignUp

class AuthViewModel(
    application: Application,
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : AndroidViewModel(application) {
    var authTarget by mutableStateOf(AuthTarget.Login)
    var signUpForm by mutableStateOf(SignUpBody("","","","",""))
    var loginForm by mutableStateOf(LoginForm("",""))
    val isFormValid
        get() = if (authTarget == AuthTarget.Login) Patterns.EMAIL_ADDRESS.matcher(loginForm.username.trim()).matches()
                && loginForm.password.isNotEmpty()
            else Patterns.EMAIL_ADDRESS.matcher(signUpForm.email.trim()).matches()
                && Patterns.PHONE.matcher(signUpForm.phone).matches()
                && signUpForm.password.length >= 6
                && signUpForm.password == signUpForm.confirmPassword
                && signUpForm.name.isNotEmpty()

    val loginStatus
        get() = sessionManager.loginStatus
    var signUpStatus: RequestState<String> by mutableStateOf(RequestState.IDLE)


    fun changeName(name:String){
        signUpForm = signUpForm.copy(name = name)
    }

    fun changeEmail(email:String){
        if (authTarget.isLogin()) loginForm = loginForm.copy(username = email)
        else signUpForm = signUpForm.copy(email = email)
    }

    fun changePassword(password:String){
        if (authTarget.isLogin()) loginForm = loginForm.copy(password = password)
        else signUpForm = signUpForm.copy(password = password)
    }
    fun changePhone(phone:String){
        signUpForm = signUpForm.copy(phone = phone)
    }
    fun changeConfirmPassword(confirmPassword:String){
        signUpForm = signUpForm.copy(confirmPassword = confirmPassword)
    }

    fun changeAuthTarget(target: AuthTarget){
        authTarget = target
        if (target.isLogin()) {
            loginForm = LoginForm("", "")
        }else{
            signUpForm = SignUpBody("","","","","")
        }
    }
    fun getName() = signUpForm.name
    fun getEmail() = if (authTarget.isLogin()) loginForm.username
        else signUpForm.email
    fun getPassword() = if (authTarget.isLogin()) loginForm.password
        else signUpForm.password
    fun getPhone() = signUpForm.phone
    fun getConfirmPassword() = signUpForm.confirmPassword

    fun login() {
        if (isFormValid){
            viewModelScope.launch {
                sessionManager.login(
                    loginForm = loginForm
                )
            }
        }
    }

    fun signUp(
        onSuccess:(String)->Unit={},
        onError:(String)->Unit={}
    ){
        if (isFormValid){
            viewModelScope.launch {
                signUpStatus = RequestState.LOADING
                signUpStatus = authRepository.signUp(signUpForm)
                if (signUpStatus.isSuccess()) onSuccess(signUpStatus.getSuccessData())
                else if (signUpStatus.isError()) onError(signUpStatus.getErrorMessage())
            }
        }
    }

    fun resetLoginStatus(){
        sessionManager.resetLoginStatus()
    }
    fun resetSignUpStatus(){
        signUpStatus = RequestState.IDLE
    }


}