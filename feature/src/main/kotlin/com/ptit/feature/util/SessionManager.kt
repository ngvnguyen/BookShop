package com.ptit.feature.util

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ptit.data.RequestState
import com.ptit.data.model.auth.login.LoginForm
import com.ptit.data.model.permisson.fetch.PermissionResponseData
import com.ptit.data.repository.AuthRepository
import com.ptit.data.repository.PermissionRepository
import com.ptit.data.repository.RoleRepository
import com.ptit.feature.permission.Permission
import com.ptit.feature.permission.toPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.distinct
import kotlin.collections.map

val Context.dataStore by preferencesDataStore("session")
class SessionManager(
    private val authRepository: AuthRepository,
    private val permissionRepository: PermissionRepository,
    private val roleRepository: RoleRepository,
    private val context: Context,
    private val applicationScope: CoroutineScope
) {
    private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    var loginStatus: MutableStateFlow<RequestState<String>> = MutableStateFlow(RequestState.IDLE);private set

    private val scope = CoroutineScope(Dispatchers.IO)
    var userId by mutableIntStateOf(-1)
    var roleId by mutableIntStateOf(-1)
    var email by mutableStateOf("")
    var permissions: MutableState<List<Permission>> = mutableStateOf(listOf())
        private set

    val accessToken = getAccessToken()
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )



    init {
        scope.launch {
            val refreshToken = getRefreshToken()
            refreshToken.filterNotNull().first().let{ token->
                loginStatus.value = RequestState.LOADING
                val refreshResponse = authRepository.loginWithRefreshToken(token)
                if (refreshResponse.isSuccess()){
                    val data = refreshResponse.getSuccessData()
                    data.first?.let { setRefreshToken(it) }
                    setAccessToken(data.second.accessToken)
                    loginStatus.value = RequestState.SUCCESS("Login successfully")
                    data.second.user.let {user->
                        setSession(
                            userId = user.id,
                            roleId = user.role.id,
                            email = user.email,
                            accessToken = data.second.accessToken
                        )
                    }
                }else {
                    loginStatus.value =
                        RequestState.ERROR(refreshResponse.getErrorMessage())
                }
            }
        }
    }
    fun resetLoginStatus(){
        loginStatus.value = RequestState.IDLE
    }

    fun login(
        loginForm: LoginForm
    ){
        scope.launch {
            loginStatus.value = RequestState.LOADING
            val loginResponse = authRepository.login(loginForm)
            if (loginResponse.isSuccess()){
                val data = loginResponse.getSuccessData()
                data.first?.let { setRefreshToken(it) }
                setAccessToken(data.second.accessToken)
                loginStatus.value = RequestState.SUCCESS("Login successfully")
                data.second.user.let {user->
                    setSession(
                        userId = user.id,
                        roleId = user.role.id,
                        email = user.email,
                        accessToken = data.second.accessToken
                    )
                }
            }else {
                val e= loginResponse.getErrorMessage()
                loginStatus.value = RequestState.ERROR(e)
            }
        }

    }

    suspend fun setSession(userId:Int, roleId:Int,email:String,accessToken: String){
        this.userId = userId
        this.roleId = roleId
        this.email = email
        loadPermissions(accessToken)
    }
    suspend fun loadPermissions(accessToken:String){
        val roleResponseData = roleRepository.getRole(accessToken,roleId)
        if (roleResponseData.isSuccess()){
            val listPermissionId = roleResponseData.getSuccessData().permissions.map { it.id }.distinct()
            val listPermissionResponseData = mutableListOf<PermissionResponseData>()
            listPermissionId.forEach { id->
                val permissionResponseData = permissionRepository.getPermission(accessToken,id)
                if (permissionResponseData.isSuccess())
                    listPermissionResponseData.add(permissionResponseData.getSuccessData())
            }
            permissions.value = listPermissionResponseData.toPermissions()
        }
    }
    suspend fun logOut(){
        userId = -1
        roleId = -1
        permissions.value = listOf()
        clearAccessToken(context)
        clearRefreshToken(context)
        loginStatus.value = RequestState.IDLE
    }

    suspend fun setAccessToken(accessToken:String){
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
        }
    }
    suspend fun setRefreshToken(refreshToken:String){
        context.dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN] = refreshToken
        }
    }
    fun getAccessToken(): Flow<String?>{
        return context.dataStore.data.map {prefs->
            prefs[ACCESS_TOKEN]
        }
    }
    fun getRefreshToken(): Flow<String?>{
        return context.dataStore.data.map {prefs->
            prefs[REFRESH_TOKEN]
        }
    }
    suspend fun clearAccessToken(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
        }
    }
    suspend fun clearRefreshToken(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(REFRESH_TOKEN)
        }
    }

    fun deletePermission(id:Int){
        permissions.value = permissions.value.map {p->
            p.copy(data = p.data.filterNot { it.id == id })
        }
    }
    fun updatePermission(permissionData: Permission.PermissionData){
        permissions.value = permissions.value.map { p->
            if (p.data.any { it.id ==permissionData.id })
                p.copy(
                    data=p.data.map {
                        if (it.id!=permissionData.id) it else permissionData
                    }
                )
            else p
        }
    }

}