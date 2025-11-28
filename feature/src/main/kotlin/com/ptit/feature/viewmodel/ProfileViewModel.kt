package com.ptit.feature.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptit.data.RequestState
import com.ptit.data.model.user.fetch.UserResponseData
import com.ptit.data.model.user.update.UpdateProfileForm
import com.ptit.data.repository.UserRepository
import com.ptit.feature.util.SessionManager
import com.ptit.feature.domain.Gender
import com.ptit.feature.util.UploadImageHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

data class ProfileForm(
    val name:String="",
    val email:String="",
    val phone:String="",
    val avatarUrl:String?=null,
    val dateOfBirth: LocalDate? = null,
    val gender:Gender= Gender.MALE
)

fun ProfileForm.toUpdateProfileForm() = UpdateProfileForm(
    avatar = avatarUrl,
    dateOfBirth = dateOfBirth.toString(),
    gender = gender.name,
    name = name,
    phone = phone
)
fun UserResponseData.toProfileForm() = ProfileForm(
    name = name,
    email = email,
    phone = phone?:"",
    avatarUrl = avatarUrl,
    dateOfBirth = dateOfBirth?.let{ LocalDate.parse(it)},
    gender = gender?.let {Gender.valueOf(it)}?: Gender.MALE
)
@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val uploadImageHelper: UploadImageHelper,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
): ViewModel() {
    private val userId = sessionManager.userId
    private val zoneId = ZoneId.systemDefault()
    private val accessTokenFlow
        get() = sessionManager.accessToken
    private var accessToken = ""

    var profileState: StateFlow<RequestState<Unit>>
        = accessTokenFlow.flatMapLatest {token->
            flow {
                emit(RequestState.LOADING)
                token?.let {
                    val profileResponse = userRepository.getUserById(token, userId)
                    if (profileResponse.isSuccess()){
                        emit(RequestState.SUCCESS(Unit))
                        profileForm = profileResponse.getSuccessData().toProfileForm()
                    }else emit(RequestState.ERROR(profileResponse.getErrorMessage()))
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )
    var profileForm by mutableStateOf<ProfileForm>(ProfileForm())
        private set
    var uploadState: RequestState<Unit> by mutableStateOf(RequestState.IDLE)

    init {
        viewModelScope.launch {
            accessToken = accessTokenFlow.filterNotNull().first()
        }
    }

    fun getLongFromDate(localDate: LocalDate?):Long?{
        return localDate
            ?.atStartOfDay(ZoneOffset.UTC)
            ?.toInstant()
            ?.toEpochMilli()
    }
    fun getDateString(localDate: LocalDate?):String{
        return localDate?.toString()?:""
    }

    fun getLocalDateFromLong(time:Long): LocalDate{
        return Instant.ofEpochMilli(time)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    fun updateName(name:String){
        profileForm = profileForm.copy(name = name)
    }
    fun updatePhone(phone:String){
        profileForm = profileForm.copy(phone = phone)
    }
    fun updateDateOfBirth(time: Long){
        profileForm = profileForm.copy(dateOfBirth = getLocalDateFromLong(time))
    }
    fun updateGender(gender: Gender){
        profileForm = profileForm.copy(gender = gender)
    }

    fun updateAvatar(
        uri: Uri,context: Context,
        onSuccess:suspend ()-> Unit,
        onError:suspend (String)->Unit
    ){
        viewModelScope.launch {
            uploadState = RequestState.LOADING
            val uploadResponse = uploadImageHelper.uploadImage(
                accessToken,
                uri,
                context.contentResolver
            )
            if (uploadResponse.isSuccess()){
                profileForm = profileForm.copy(
                    avatarUrl = uploadResponse.getSuccessData().urlFile
                )
                uploadState = RequestState.SUCCESS(Unit)
                onSuccess()
            }
            else {
                val errMsg = uploadResponse.getErrorMessage()
                uploadState = RequestState.ERROR(errMsg)
                onError(errMsg)
            }
        }
    }
    fun resetUploadState(){
        uploadState = RequestState.IDLE
    }

    fun updateProfile(
        onSuccess: suspend () -> Unit,
        onError: suspend (String) -> Unit
    ){
        viewModelScope.launch {
            val response = userRepository.updateProfile(accessToken,userId,profileForm.toUpdateProfileForm())
            if (response.isSuccess()){
                onSuccess()
            }else onError(response.getErrorMessage())
        }
    }

}

