package com.ptit.feature.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptit.data.RequestState
import com.ptit.feature.SessionManager
import com.ptit.data.model.permisson.fetch.PermissionResponseData
import com.ptit.data.repository.AuthRepository
import com.ptit.data.repository.BookRepository
import com.ptit.data.repository.CategoryRepository
import com.ptit.data.repository.PermissionRepository
import com.ptit.data.repository.PublisherRepository
import com.ptit.data.repository.RoleRepository
import com.ptit.feature.form.BookForm
import com.ptit.feature.form.CategoryForm
import com.ptit.feature.form.toBookForm
import com.ptit.feature.form.toCategoryForm
import com.ptit.feature.permission.Permission
import com.ptit.feature.permission.toPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
    private val categoryRepository: CategoryRepository,
    private val bookRepository: BookRepository,
    private val publisherRepository: PublisherRepository
): ViewModel() {
    var searchQuery by mutableStateOf("")
    val permissions get() = sessionManager.permissions
    val isAdminUnlocked
        get() = permissions.value.isNotEmpty()
    private val accessTokenFlow
        get() = sessionManager.accessToken
    private var accessToken = ""

    private val refreshTrigger = MutableStateFlow(0)



    val allCategory: StateFlow<RequestState<List<CategoryForm>>> =
        combine(accessTokenFlow,refreshTrigger) { token, _->
            token
        }.flatMapLatest { token->
            flow {
                emit(RequestState.LOADING)
                token?.let {
                    val response = categoryRepository.getAllCategory(it)
                    if (response.isSuccess())
                        emit(RequestState.SUCCESS(response.getSuccessData()
                            .map { it.toCategoryForm() }
                            .filter { it.status!= CategoryForm.Status.INACTIVE }))
                    else emit(RequestState.ERROR(response.getErrorMessage()))
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )

    val bookPaged: StateFlow<RequestState<List<BookForm>>> = combine(accessTokenFlow,refreshTrigger) {token,_->
        token
    }.flatMapLatest { token->
        flow {
            emit(RequestState.LOADING)
            token?.let {
                val response = bookRepository.searchBookPaged(token,1,"","","")
                if (response.isSuccess()){
                    val data = response.getSuccessData()
                    emit(
                        RequestState.SUCCESS(
                            data.books.map { it.toBookForm(page =data.page, maxPage = data.pages) }
                        )
                    )
                }
                else emit(RequestState.ERROR(response.getErrorMessage()))
            }
        }.flowOn(Dispatchers.IO)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.LOADING
    )

    init{
        viewModelScope.launch {
            accessToken = accessTokenFlow.filterNotNull().first()
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun signOut(
        onSuccess:(String)->Unit,
        onError:(String)->Unit
    ){
        viewModelScope.launch {
            println(accessToken)
            val result = authRepository.signOut(accessToken)
            if (result.isSuccess()) {
                sessionManager.logOut()
                onSuccess(result.getSuccessData())
            }
            else onError(result.getErrorMessage())

        }
    }
}