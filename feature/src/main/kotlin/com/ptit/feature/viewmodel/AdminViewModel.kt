package com.ptit.feature.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptit.data.RequestState
import com.ptit.data.repository.AuthorRepository
import com.ptit.data.repository.BookRepository
import com.ptit.data.repository.CategoryRepository
import com.ptit.data.repository.DashboardRepository
import com.ptit.data.repository.PermissionRepository
import com.ptit.data.repository.PublisherRepository
import com.ptit.data.repository.RoleRepository
import com.ptit.data.repository.UserRepository
import com.ptit.feature.util.SessionManager
import com.ptit.feature.permission.Permission
import com.ptit.feature.permission.toPermissionData
import com.ptit.feature.permission.toPermissions
import com.ptit.feature.domain.Gender
import com.ptit.feature.form.AuthorForm
import com.ptit.feature.form.BookForm
import com.ptit.feature.form.CategoryForm
import com.ptit.feature.form.PermissionForm
import com.ptit.feature.form.PublisherForm
import com.ptit.feature.form.RoleForm
import com.ptit.feature.form.UserForm
import com.ptit.feature.form.toAuthorForm
import com.ptit.feature.form.toBookForm
import com.ptit.feature.form.toCategoryForm
import com.ptit.feature.form.toCreateBookForm
import com.ptit.feature.form.toCreateCategoryForm
import com.ptit.feature.form.toCreatePermissionForm
import com.ptit.feature.form.toCreateRoleForm
import com.ptit.feature.form.toCreateUserForm
import com.ptit.feature.form.toPermissionForm
import com.ptit.feature.form.toPublisherForm
import com.ptit.feature.form.toRoleForm
import com.ptit.feature.form.toUpdateBookForm
import com.ptit.feature.form.toUpdatePermissionForm
import com.ptit.feature.form.toUpdateRoleForm
import com.ptit.feature.form.toUpdateUserForm
import com.ptit.feature.form.toUserForm
import com.ptit.feature.util.UploadImageHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

enum class Action{
    IDLE,
    CREATE,
    UPDATE;
    fun isIdle() = this == IDLE
    fun isUpdate() = this == UPDATE
    fun isCreate() = this == CREATE
}
fun getStatus(isActive: Boolean) = if (isActive) "ACTIVE" else "INACTIVE"
data class BookSearchParams(
    val page:Int=1,
    val name:String="",
    val categoryQuery:String="",
    val authorQuery:String=""
)


@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class AdminViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val authorRepository: AuthorRepository,
    private val bookRepository: BookRepository,
    private val categoryRepository: CategoryRepository,
    private val permissionRepository: PermissionRepository,
    private val publisherRepository: PublisherRepository,
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    private val dashboardRepository: DashboardRepository,
    private val uploadImageHelper: UploadImageHelper,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val accessTokenFlow
        get()= sessionManager.accessToken
    private val email = sessionManager.email
    private var accessToken:String=""
    private val zoneId = ZoneId.systemDefault()

    val rolePermission
        get() = sessionManager.permissions

    val overviewData = accessTokenFlow.flatMapLatest { token->
        flow{
            emit(RequestState.LOADING)
            if (token!=null){
                val response = dashboardRepository.getOverviewData(token)
                emit(response)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.LOADING
    )

    val revenueByMonthData = accessTokenFlow.flatMapLatest { token->
        flow{
            emit(RequestState.LOADING)
            if (token!=null){
                val response = dashboardRepository.getRevenueByMonth(token)
                emit(response)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.LOADING
    )

    val orderStatusCountData = accessTokenFlow.flatMapLatest { token->
        flow{
            emit(RequestState.LOADING)
            if (token!=null){
                val response = dashboardRepository.getOrderStatusCount(token)
                emit(response)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.LOADING
    )

    val permissionRefreshTrigger = MutableStateFlow(0)
    val allPermission = combine(accessTokenFlow,permissionRefreshTrigger){ token, _->token}.flatMapLatest { token->
        flow {
            if (token!=null){
                val allPermissionResponse = permissionRepository.getAllPermission(token)
                if (allPermissionResponse.isSuccess()){
                    emit(RequestState.SUCCESS(allPermissionResponse.getSuccessData().toPermissions()))
                }
                else{
                    emit(RequestState.ERROR(allPermissionResponse.getErrorMessage()))
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.LOADING
    )
    val mapPermissionFlow = allPermission.flatMapLatest { state ->
        flow {
            emit(
                if (state.isSuccess()) {
                    state.getSuccessData()
                        .flatMap { it.data }
                        .associate { it.id to it.name }
                } else mapOf()
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = mapOf()
    )


    val roleRefreshTrigger = MutableStateFlow(0)
    val allRole : StateFlow<RequestState<List<RoleForm>>>
        = combine(accessTokenFlow,roleRefreshTrigger){ token, _->token}.flatMapLatest { token->
            flow {
                if (token!=null){
                    val allRoleResponse = roleRepository.getAllRole(token)
                    if (allRoleResponse.isSuccess()){
                        val allRoleForm = allRoleResponse.getSuccessData().map { it.toRoleForm() }
                        emit(RequestState.SUCCESS(allRoleForm))
                    }
                    else{
                        emit(RequestState.ERROR(allRoleResponse.getErrorMessage()))
                    }
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )


    val bookRefreshTrigger = MutableStateFlow(0)
    private val bookSearchQuery = MutableStateFlow(BookSearchParams())
    var medialBookSearchQuery by mutableStateOf(BookSearchParams())
    val bookPaged = combine(bookSearchQuery,accessTokenFlow,bookRefreshTrigger) { query,token,_->
        token to query
    }.flatMapLatest { (token,query)->
        flow {
            emit(RequestState.LOADING)
            token?.let {
                val response = bookRepository.searchBookPaged(
                    accessToken =it,
                    page = query.page,
                    name = query.name,
                    categoryQuery = query.categoryQuery,
                    authorQuery = query.authorQuery
                )
                if (response.isSuccess()){
                    val data = response.getSuccessData()
                    val allBookForm = data.books.map { it.toBookForm(data.page,data.pages) }
                    emit(RequestState.SUCCESS(allBookForm))
                }else emit(RequestState.ERROR(response.getErrorMessage()))
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.LOADING
    )



    var userSearchQuery = MutableStateFlow("");private set
    var userPageQuery = MutableStateFlow(1);private set
    // Trigger for refreshing data
    var refreshUserTrigger = MutableStateFlow(1);private set
    val userPaged: StateFlow<RequestState<List<UserForm>>> =
        combine(userSearchQuery.debounce(500), userPageQuery, refreshUserTrigger,accessTokenFlow) {
            query, page,_ ,_-> query to page
        }.flatMapLatest { (query,p)->
            flow {
                emit(RequestState.LOADING)
                accessTokenFlow.value?.let {
                    val name = if (query.isBlank()) null else query
                    val response = userRepository.getUserPaged(it,p,name)
                    if (response.isSuccess()){
                        val data = response.getSuccessData()
                        val listUserForm = data.users.map { it.toUserForm(data.page,data.pages) }
                        emit(RequestState.SUCCESS(listUserForm))
                    }else
                        emit(RequestState.ERROR(response.getErrorMessage()))
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )

    var refreshCategoryTrigger = MutableStateFlow(0)
    val allCategory: StateFlow<RequestState<List<CategoryForm>>> =
        combine(accessTokenFlow,refreshCategoryTrigger){ token, _->
            token
        }.flatMapLatest {token->
            flow {
                emit(RequestState.LOADING)
                token?.let {
                    val response = categoryRepository.getAllCategory(it)
                    if (response.isSuccess()) emit(RequestState.SUCCESS(response.getSuccessData().map { it.toCategoryForm() }))
                    else emit(RequestState.ERROR(response.getErrorMessage()))
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )

    var refreshAuthorTrigger = MutableStateFlow(0)
    val allAuthor: StateFlow<RequestState<List<AuthorForm>>> =
        combine(accessTokenFlow,refreshAuthorTrigger){ token, _->
            token
        }.flatMapLatest {token->
            flow {
                emit(RequestState.LOADING)
                token?.let {
                    val response = authorRepository.getAllAuthor(it)
                    if (response.isSuccess()) emit(RequestState.SUCCESS(response.getSuccessData().map { it.toAuthorForm() }))
                    else emit(RequestState.ERROR(response.getErrorMessage()))
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )

    var refreshPublisherTrigger = MutableStateFlow(0)
    val allPublisher: StateFlow<RequestState<List<PublisherForm>>> =
        combine(accessTokenFlow,refreshPublisherTrigger){ token, _->
            token
        }.flatMapLatest {token->
            flow {
                emit(RequestState.LOADING)
                token?.let {
                    val response = publisherRepository.getAllPublisher(it)
                    if (response.isSuccess()) emit(RequestState.SUCCESS(response.getSuccessData().map { it.toPublisherForm() }))
                    else emit(RequestState.ERROR(response.getErrorMessage()))
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )

    var permissionForm by mutableStateOf(PermissionForm(createdBy = email));private set
    val isPermissionFormValid :Boolean
        get() = permissionForm.name.isNotBlank() &&
                permissionForm.apiPath.isNotBlank()

    var userForm by mutableStateOf(UserForm());private set
    val isUserFormValid : Boolean
        get() = userForm.name.isNotBlank() &&
                userForm.email.isNotBlank()


    var roleForm by mutableStateOf(RoleForm(createdBy = email));private set
    val isRoleFormValid : Boolean
        get() = roleForm.name.isNotBlank() &&
                roleForm.description.isNotBlank()

    var bookForm by mutableStateOf(BookForm());private set
    val isBookFormValid : Boolean
        get() = bookForm.name.isNotBlank() &&
                bookForm.author != null &&
                bookForm.publisher != null &&
                bookForm.category != null &&
                bookForm.price > 0.0 &&
                bookForm.language.isNotBlank()

    var categoryForm by mutableStateOf(CategoryForm());private set

    var action by mutableStateOf(Action.IDLE)
    var uploadState: RequestState<Unit> by mutableStateOf(RequestState.IDLE)

    init {
        viewModelScope.launch {
            accessToken = accessTokenFlow.filterNotNull().first()
        }
    }


    fun setAct(newAction: Action){
        action = newAction
    }
    fun resetAction(){
        action = Action.IDLE
    }
    fun updatePermissionForm(
        name:String?=null,
        isActive:Boolean?=null,
        apiPath:String?=null,
        method: Permission.Method?=null,
        module: Permission.Module?=null
    ) {
        permissionForm = permissionForm.copy(
            name = name ?: permissionForm.name,
            isActive = isActive ?: permissionForm.isActive,
            apiPath = apiPath ?: permissionForm.apiPath,
            method = method ?: permissionForm.method,
            module = module ?: permissionForm.module
        )
    }
    fun selectPermission(
        permissionData: Permission.PermissionData,
        module: Permission.Module
    ){
        permissionForm = permissionData.toPermissionForm(module)
    }
    fun resetPermissionForm(){
        permissionForm = PermissionForm(createdBy = email)
    }
    fun createPermission(
        onSuccess:suspend ()->Unit,
        onError:suspend (String)->Unit
    ){
        viewModelScope.launch {
            val createPermissionForm = permissionForm.toCreatePermissionForm()
            val result = permissionRepository.createPermission(accessToken,createPermissionForm)
            if (result.isSuccess()){
                onSuccess()
                permissionRefreshTrigger.value++
            }else{
                onError(result.getErrorMessage())
            }
        }
    }
    fun updatePermission(
        onSuccess:suspend ()->Unit,
        onError:suspend (String)->Unit
    ){
        viewModelScope.launch {
            val updatePermissionForm = permissionForm.toUpdatePermissionForm()
            permissionForm.id?.let{id->
                val result = permissionRepository.updatePermission(accessToken,id,updatePermissionForm)
                if (result.isSuccess()){
                    onSuccess()
                    val id = result.getSuccessData()
                    val newPermissionResponse = permissionRepository.getPermission(accessToken,id)
                    permissionRefreshTrigger.value++
                    if (newPermissionResponse.isSuccess()){
                        val newPermissionData = newPermissionResponse.getSuccessData()
                        sessionManager.updatePermission(newPermissionData.toPermissionData())
                    }
                }else{
                    onError(result.getErrorMessage())
                }
            }?:onError("Permission ID is null")
        }
    }

    fun deletePermission(
        id: Int,
        onSuccess: suspend () -> Unit,
        onError: suspend (String) -> Unit
    ){
        viewModelScope.launch {
            val result = permissionRepository.deletePermission(accessToken,id)
            if (result.isSuccess()){
                onSuccess()
                permissionRefreshTrigger.value++
                sessionManager.deletePermission(id)
            }else onError(result.getErrorMessage())
        }
    }

    fun setUserSearchQuery(query:String){
        userSearchQuery.value = query
    }

    fun selectUser(userForm: UserForm){
        this.userForm = userForm
    }
    fun resetUser(){
        userForm = UserForm()
    }

    fun updateUserName(name:String){
        userForm = userForm.copy(name = name)
    }
    fun updateUserPhone(phone:String){
        userForm = userForm.copy(phone = phone)
    }
    fun updateUserPassword(password: String){
        userForm = userForm.copy(password = password)
    }
    fun updateUserEmail(email:String){
        userForm = userForm.copy(email = email)
    }

    fun updateUserStatus(isActive: Boolean){
        userForm = userForm.copy(isActive = isActive)
    }
    fun updateUserGender(gender: Gender){
        userForm = userForm.copy(gender = gender)
    }
    fun updateUserDateOfBirth(time: Long){
        userForm = userForm.copy(dateOfBirth = getLocalDateFromLong(time))
    }
    fun updateUserAvatar(
        uri: Uri,
        context: Context,
        onSuccess: suspend () -> Unit,
        onError: suspend (String)-> Unit
    ){
        viewModelScope.launch {
            uploadState = RequestState.LOADING
            val uploadResponse = uploadImageHelper.uploadImage(
                accessToken,
                uri,
                context.contentResolver
            )
            if (uploadResponse.isSuccess()){
                userForm = userForm.copy(
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
    fun updateUser(
        onSuccess: suspend () -> Unit,
        onError: suspend (String) -> Unit
    ){
        viewModelScope.launch {
            val response = userRepository.updateUser(accessToken,userForm.id,userForm.toUpdateUserForm())
            if (response.isSuccess()){
                onSuccess()
                refreshUserTrigger.value++
            }else onError(response.getErrorMessage())
        }
    }
    fun createUser(
        onSuccess: suspend () -> Unit,
        onError: suspend (String)-> Unit
    ){
        viewModelScope.launch {
            val response = userRepository.createUser(accessToken,userForm.toCreateUserForm())
            if (response.isSuccess()){
                onSuccess()
                refreshUserTrigger.value++
            }else onError(response.getErrorMessage())
        }
    }

    fun getLongFromDate(localDate: LocalDate?):Long?{
        return localDate
            ?.atStartOfDay(ZoneOffset.UTC)
            ?.toInstant()
            ?.toEpochMilli()
    }
    fun getDateString(localDate: LocalDate):String{
        return localDate.toString()
    }

    fun getLocalDateFromLong(time:Long): LocalDate{
        return LocalDate.ofEpochDay(time)
    }
    fun resetUploadState(){
        uploadState = RequestState.IDLE
    }
    fun updatePage(page:Int){
        this.userPageQuery.value = page
    }

    fun selectRoleForm(
        roleForm: RoleForm
    ){
        this.roleForm = roleForm
    }
    fun resetRoleForm(){
        roleForm = RoleForm(createdBy = email)
    }
    fun updateRoleName(name:String){
        roleForm = roleForm.copy(name = name)
    }
    fun updateRoleDescription(description:String){
        roleForm = roleForm.copy(description = description)
    }
    fun updateRoleStatus(status: RoleForm.Status){
        roleForm = roleForm.copy(status = status)
    }
    fun deletePermissionInRole(permissionId: Int){
        roleForm = roleForm.copy(
            listPermission = roleForm.listPermission - permissionId
        )
    }
    fun addPermissionInRole(permissionId: Int, permissionName: String){
        roleForm = roleForm.copy(
            listPermission = roleForm.listPermission + (permissionId to permissionName)
        )
    }
    fun createRole(
        onSuccess: suspend () -> Unit,
        onError: suspend (String) -> Unit
    ){
        viewModelScope.launch {
            val response = roleRepository.createRole(
                accessToken = accessToken,
                createRoleForm = roleForm.toCreateRoleForm()
            )
            if (response.isSuccess()){
                onSuccess()
                roleRefreshTrigger.value++
            }else onError(response.getErrorMessage())
        }
    }
    fun updateRole(
        onSuccess: suspend () -> Unit,
        onError: suspend (String) -> Unit
    ){
        viewModelScope.launch {
            val response = roleRepository
                .updateRole(
                    accessToken = accessToken,
                    id = roleForm.id,
                    updateRoleForm = roleForm.toUpdateRoleForm()
                )
            if (response.isSuccess()){
                onSuccess()
                roleRefreshTrigger.value++
            }else onError(response.getErrorMessage())
        }
    }
    fun deleteRole(
        id: Int,
        onSuccess: suspend () -> Unit,
        onError: suspend (String) -> Unit
    ){
        viewModelScope.launch {
            val response = roleRepository.deleteRole(
                accessToken = accessToken,
                id = id
            )
            if (response.isSuccess()){
                onSuccess()
                roleRefreshTrigger.value++
            }else onError(response.getErrorMessage())
        }
    }

    fun selectBookForm(bookForm: BookForm){
        this.bookForm = bookForm
    }

    fun updateBookSearchParams(
        page:Int?=null,
        name:String?=null,
        categoryQuery:String?=null,
        authorQuery:String?=null
    ){
        medialBookSearchQuery = BookSearchParams(
            page = page?:medialBookSearchQuery.page,
            name = name?:medialBookSearchQuery.name,
            categoryQuery = categoryQuery?:medialBookSearchQuery.categoryQuery,
            authorQuery = authorQuery?:medialBookSearchQuery.authorQuery
        )
    }

    fun resetBookForm(){
        bookForm = BookForm()
    }

    fun updateBookForm(
        name:String?=null,
        description: String?=null,
        author: Pair<Int,String>?=null,
        publisher: Pair<Int,String>?=null,
        category: Pair<Int,String>?=null,
        price: Double?=null,
        discount: Double?=null,
        quantity: Int?=null,
        status: BookForm.Status?=null,
        language:String?=null
    ){
        bookForm = bookForm.copy(
            name = name?:bookForm.name,
            description = description?:bookForm.description,
            author = author?.let {(id,name)-> BookForm.Author(id,name) }
                ?:bookForm.author,
            publisher = publisher?.let {(id,name)-> BookForm.Publisher(id,name) }
                ?:bookForm.publisher,
            category = category?.let {(id,name)-> BookForm.Category(id,name) }
                ?:bookForm.category,
            price = price?:bookForm.price,
            discount = discount?:bookForm.discount,
            quantity = quantity?:bookForm.quantity,
            status = status?:bookForm.status,
            language = language?:bookForm.language
        )
    }

    fun submitSearchBookParams(){
        bookSearchQuery.value = medialBookSearchQuery
    }
    fun updateBookImage(
        uri: Uri,
        context: Context,
        onSuccess: suspend () -> Unit,
        onError: suspend (String) -> Unit
    ){
        viewModelScope.launch {
            uploadState = RequestState.LOADING
            val uploadResponse = uploadImageHelper.uploadImage(
                accessToken,
                uri,
                context.contentResolver
            )
            if (uploadResponse.isSuccess()){
                bookForm = bookForm.copy(
                    image = uploadResponse.getSuccessData().urlFile
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

    fun createBook(
        onSuccess: suspend () -> Unit,
        onError: suspend (String) -> Unit
    ){
        viewModelScope.launch {
            try{
                val response = bookRepository.createBook(
                    accessToken = accessToken,
                    bookForm = bookForm.toCreateBookForm()
                )
                if (response.isSuccess()){
                    onSuccess()
                    bookRefreshTrigger.value++
                }else onError(response.getErrorMessage())
            }catch (e: Exception) {
                onError(e.message.toString())
            }
        }
    }
    fun updateBook(
        onSuccess: suspend () -> Unit,
        onError: suspend (String) -> Unit
    ){
        viewModelScope.launch {
            try{
                val response = bookRepository.updateBook(
                    accessToken = accessToken,
                    bookForm = bookForm.toUpdateBookForm(),
                    bookId = bookForm.id
                )
                if (response.isSuccess()){
                    onSuccess()
                    bookRefreshTrigger.value++
                }else onError(response.getErrorMessage())
            }catch (e: Exception){
                onError(e.message.toString())
            }
        }
    }



    fun refreshRole(){
        roleRefreshTrigger.value++
    }

    fun selectCategoryForm(categoryForm: CategoryForm){
        this.categoryForm = categoryForm
    }
    fun resetCategoryForm(){
        categoryForm = CategoryForm()
    }
    fun updateCategoryForm(
        name: String? = null,
        description: String? = null,
        status: CategoryForm.Status? = null
    ){
        categoryForm = categoryForm.copy(
            name = name ?: categoryForm.name,
            description = description ?: categoryForm.description,
            status = status ?: categoryForm.status
        )
    }

    fun createCategory(
        onSuccess: suspend () -> Unit,
        onError: suspend (String) ->Unit
    ){
        viewModelScope.launch {
            val response = categoryRepository.createCategory(
                accessToken = accessToken,
                createCategoryForm = categoryForm.toCreateCategoryForm()
            )
            if (response.isSuccess()){
                onSuccess()
                refreshCategoryTrigger.value++
            }else onError(response.getErrorMessage())
        }
    }
}