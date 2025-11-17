package com.ptit.feature.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptit.data.RequestState
import com.ptit.data.model.cart.CartData
import com.ptit.data.model.checkout.CheckoutRequest
import com.ptit.data.repository.AddressRepository
import com.ptit.feature.util.SessionManager
import com.ptit.data.repository.AuthRepository
import com.ptit.data.repository.BookRepository
import com.ptit.data.repository.CartRepository
import com.ptit.data.repository.CategoryRepository
import com.ptit.data.repository.PublisherRepository
import com.ptit.feature.domain.FilterPriceItem
import com.ptit.feature.form.AddressForm
import com.ptit.feature.form.BookForm
import com.ptit.feature.form.CategoryForm
import com.ptit.feature.form.toBookForm
import com.ptit.feature.form.toCategoryForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.ptit.feature.form.toAddressForm
import com.ptit.feature.util.SharedState

data class BookFilter(
    val name:String="",
    val category:String="",
    val author: String="",
    val upperPrice:Int?=null,
    val lowerPrice:Int?=null
)

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
    private val categoryRepository: CategoryRepository,
    private val bookRepository: BookRepository,
    private val publisherRepository: PublisherRepository,
    private val cartRepository: CartRepository,
    private val addressRepository: AddressRepository,
    private val savedStateHandle: SavedStateHandle,
    private val sharedState: SharedState
): ViewModel() {
    val permissions get() = sessionManager.permissions
    val isAdminUnlocked
        get() = permissions.value.isNotEmpty()
    private val accessTokenFlow
        get() = sessionManager.accessToken
    private var accessToken = ""
    val couponCode:String?
        get() = sharedState.couponCode

    private val bookRefreshTrigger = MutableStateFlow(0)
    var isRefreshing by mutableStateOf(false)
    val addressId = snapshotFlow { sharedState.addressId }

    val allCategory: StateFlow<RequestState<List<CategoryForm>>> =
        combine(accessTokenFlow,bookRefreshTrigger) { token, _->
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
    var bookName by mutableStateOf("")
    val bookFilter = MutableStateFlow(BookFilter())
    val bookPaged: StateFlow<RequestState<List<BookForm>>>
        = combine(accessTokenFlow,bookFilter,bookRefreshTrigger) { token, bookFilter, _->
        token to bookFilter
    }.flatMapLatest { (token,bookFilter)->
        flow {
            emit(RequestState.LOADING)
            token?.let {
                val response = bookRepository.searchBookPaged(
                    accessToken = token,
                    page = 1,
                    pageSize = 21,
                    name = bookFilter.name,
                    categoryQuery = bookFilter.category,
                    authorQuery = bookFilter.author,
                    lowerPrice = bookFilter.lowerPrice,
                    upperPrice = bookFilter.upperPrice
                )
                if (response.isSuccess()){
                    val data = response.getSuccessData()
                    emit(
                        RequestState.SUCCESS(
                            data.books.map { it.toBookForm(page =data.page, maxPage = data.pages) }
                        )
                    )
                }
                else {
                    emit(RequestState.ERROR(response.getErrorMessage()))
                }
            }
        }.flowOn(Dispatchers.IO)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = RequestState.LOADING
    )

    val homeRefreshTrigger = MutableStateFlow(0)
    val newestBook = combine(accessTokenFlow,homeRefreshTrigger) {token,_->token  }
        .flatMapLatest { token->
            flow {
                emit(RequestState.LOADING)
                if (token!=null){
                    val response = bookRepository.getNewestBook(token)
                    if (response.isSuccess()) emit(RequestState.SUCCESS(response.getSuccessData().books.map { it.toBookForm(0,0) }))
                    else emit(RequestState.ERROR(response.getErrorMessage()))
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )
    val discountedBook = combine(accessTokenFlow,homeRefreshTrigger) {token,_->token  }
        .flatMapLatest { token->
            flow {
                emit(RequestState.LOADING)
                if (token!=null){
                    val response = bookRepository.getDiscountedBook(token)
                    if (response.isSuccess()) emit(RequestState.SUCCESS(response.getSuccessData().books.map { it.toBookForm(0,0) }))
                    else emit(RequestState.ERROR(response.getErrorMessage()))
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.LOADING
        )

    var cartSearchQuery = MutableStateFlow("") ;private set
    val cartRefreshTrigger = MutableStateFlow(0)
    private val cart: StateFlow<RequestState<CartData>> = combine(cartRefreshTrigger,accessTokenFlow){_,token->
        token
    }.flatMapLatest {token->
        flow {
            token?.let {
                val response = cartRepository.getCart(token)
                emit(response)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.LOADING
    )
    val isCartEmpty: StateFlow<Boolean> = cart.map { cartState->
        if (cartState.isSuccess()) cartState.getSuccessData().cartItems.isEmpty()
        else true
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    val cartItemFilter: StateFlow<RequestState<List<CartData.CartItem>>> = combine(cartSearchQuery,cart) {query,cart->
        query to cart
    }.flatMapLatest { (query,cart)->
        flow {
            if (cart.isSuccess()){
                val cartItems = cart.getSuccessData().cartItems.filter {
                    if (query.isBlank()) true
                    else it.productName.lowercase().contains(query.lowercase())
                }
                emit(RequestState.SUCCESS(cartItems))
            }else if (cart.isLoading()) emit(RequestState.LOADING)
            else emit(RequestState.ERROR(cart.getErrorMessage()))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.LOADING
    )
    private val allAddress : StateFlow<RequestState<List<AddressForm>>>
        = combine(accessTokenFlow,addressId,cartRefreshTrigger){token,_,_-> token
    }.flatMapLatest { token->
        flow {
            emit(RequestState.LOADING)
            if (token!=null){
                val response = addressRepository.getAllAddress(accessToken)
                if (response.isSuccess())
                    emit(RequestState.SUCCESS(response.getSuccessData().map { it.toAddressForm() }))
                else emit(RequestState.ERROR(response.getErrorMessage()))
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.LOADING
    )
    val address : StateFlow<AddressForm?> = allAddress.map { all->
        if (all.isSuccess()) all.getSuccessData().firstOrNull { it.isDefault }
        else null
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val selectedIds = mutableStateSetOf<Int>()

    init{
        viewModelScope.launch {
            accessToken = accessTokenFlow.filterNotNull().first()
        }
    }

    var filterPriceItem : FilterPriceItem? by mutableStateOf(null)
    fun selectFilterPriceItem(item: FilterPriceItem?){
        filterPriceItem = item
    }
    fun submitFilter(){
        val lower = filterPriceItem?.maxPrice?.replace(".","")?.toIntOrNull()
        val upper = filterPriceItem?.minPrice?.replace(".","")?.toIntOrNull()
        bookFilter.value = bookFilter.value.copy(upperPrice = upper, lowerPrice = lower)
    }
    fun updateBookFilterName(name:String){
        bookName = name
    }
    fun submitBookFilterName(){
        bookFilter.value = bookFilter.value.copy(name = bookName)
    }

    fun onBookFilterCategory(category:String){
        if (bookFilter.value.category==category) bookFilter.value = bookFilter.value.copy(category="")
        else bookFilter.value = bookFilter.value.copy(category=category)
    }

    fun addBookFilterAuthor(author:String){
//        val current = bookFilter.value.author.toMutableList()
//        if (current.contains(author)){
//            current.remove(author)
//            bookFilter.value = bookFilter.value.copy(author = current)
//        }
//        else {
//            current.add(author)
//            bookFilter.value = bookFilter.value.copy(author = current)
//        }
    }
    fun updateCartSearchQuery(query: String){
        cartSearchQuery.value = query
    }
    fun homeRefresh(){
        viewModelScope.launch {
            isRefreshing = true
            homeRefreshTrigger.value++
            delay(100)
            isRefreshing = false
        }
    }
    fun bookRefresh(){
        viewModelScope.launch {
            isRefreshing = true
            bookRefreshTrigger.value++
            delay(100)
            isRefreshing = false
        }
    }
    fun cartRefresh(){
        viewModelScope.launch {
            isRefreshing = true
            cartRefreshTrigger.value++
            delay(100)
            isRefreshing = false
        }
    }
    fun updateCartItemQuantity(
        productId:Int,quantity:Int,
        onSuccess:suspend ()->Unit,
        onError:suspend (String)-> Unit
    ){
        viewModelScope.launch {
            val result = cartRepository.updateCartItem(
                accessToken = accessToken,
                productId = productId,
                quantity = quantity
            )
            if (result.isSuccess()){
                onSuccess()
                cartRefreshTrigger.value++
            }else onError(result.getErrorMessage())
        }
    }
    fun deleteCartItem(
        productId:Int,
        onSuccess: suspend () -> Unit,
        onError: suspend (String) -> Unit
    ){
        viewModelScope.launch {
            val result = cartRepository.deleteCartItem(
                accessToken = accessToken,
                productId = productId
            )
            if (result.isSuccess()){
                onSuccess()
                if (selectedIds.contains(productId)){
                    selectedIds.remove(productId)
                }
                cartRefreshTrigger.value++
            }else onError(result.getErrorMessage())
        }
    }
    fun selectCartItem(productId: Int){
        selectedIds.add(productId)
    }
    fun removeCartItemSelection(productId: Int){
        selectedIds.remove(productId)
    }
    fun clearAllCartItemSelection(){
        selectedIds.clear()
    }
    fun selectAllCartItem(){
        clearAllCartItemSelection()
        val ids = if (cartItemFilter.value.isSuccess()){
            cartItemFilter.value.getSuccessData().map { it.id }
        }else listOf()
        selectedIds.addAll(ids)
    }

    fun checkout(){
        val ids = selectedIds.toList().map { CheckoutRequest.CartItem(it) }
        val discountCode = couponCode
        val checkoutRequest = CheckoutRequest(ids,discountCode)
        sharedState.checkoutRequest = checkoutRequest
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