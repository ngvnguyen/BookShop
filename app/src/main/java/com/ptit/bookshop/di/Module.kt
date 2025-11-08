package com.ptit.bookshop.di

import com.ptit.data.repository.AddressRepository
import com.ptit.data.repository.AuthRepository
import com.ptit.data.repository.AuthorRepository
import com.ptit.data.repository.BookRepository
import com.ptit.data.repository.CartRepository
import com.ptit.data.repository.CategoryRepository
import com.ptit.data.repository.CheckoutRepository
import com.ptit.data.repository.CouponRepository
import com.ptit.data.repository.FileRepository
import com.ptit.data.repository.PermissionRepository
import com.ptit.data.repository.PublisherRepository
import com.ptit.data.repository.RoleRepository
import com.ptit.data.repository.UserRepository
import com.ptit.data.repository.impl.AddressRepositoryImpl
import com.ptit.data.repository.impl.AuthRepositoryImpl
import com.ptit.data.repository.impl.AuthorRepositoryImpl
import com.ptit.data.repository.impl.BookRepositoryImpl
import com.ptit.data.repository.impl.CartRepositoryImpl
import com.ptit.data.repository.impl.CategoryRepositoryImpl
import com.ptit.data.repository.impl.CheckoutRepositoryImpl
import com.ptit.data.repository.impl.CouponRepositoryImpl
import com.ptit.data.repository.impl.FileRepositoryImpl
import com.ptit.data.repository.impl.PermissionRepositoryImpl
import com.ptit.data.repository.impl.PublisherRepositoryImpl
import com.ptit.data.repository.impl.RoleRepositoryImpl
import com.ptit.data.repository.impl.UserRepositoryImpl
import com.ptit.data.retrofit.RetrofitInstance
import com.ptit.feature.util.SessionManager
import com.ptit.feature.util.SharedState
import com.ptit.feature.util.UploadImageHelper
import com.ptit.feature.viewmodel.AddressViewModel
import com.ptit.feature.viewmodel.AdminViewModel
import com.ptit.feature.viewmodel.AuthViewModel
import com.ptit.feature.viewmodel.BookDetailsViewModel
import com.ptit.feature.viewmodel.ChangePasswordViewModel
import com.ptit.feature.viewmodel.CheckoutViewModel
import com.ptit.feature.viewmodel.CouponViewModel
import com.ptit.feature.viewmodel.HomeViewModel
import com.ptit.feature.viewmodel.ProfileViewModel
import com.ptit.feature.viewmodel.ResetPasswordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val module = module{
    single<AuthRepository> { AuthRepositoryImpl(RetrofitInstance.authApi) }
    single<RoleRepository>{ RoleRepositoryImpl(RetrofitInstance.roleApi) }
    single<PermissionRepository>{ PermissionRepositoryImpl(RetrofitInstance.permissionApi) }
    single<AuthorRepository>{ AuthorRepositoryImpl(RetrofitInstance.authorApi) }
    single<CategoryRepository>{ CategoryRepositoryImpl(RetrofitInstance.categoryApi) }
    single<BookRepository>{ BookRepositoryImpl(RetrofitInstance.bookApi) }
    single<PublisherRepository>{ PublisherRepositoryImpl(RetrofitInstance.publisherApi) }
    single<FileRepository>{ FileRepositoryImpl(RetrofitInstance.fileApi) }
    single<UserRepository>{ UserRepositoryImpl(RetrofitInstance.userApi) }
    single<CartRepository> { CartRepositoryImpl(RetrofitInstance.cartApi) }
    single<AddressRepository> { AddressRepositoryImpl(RetrofitInstance.addressApi) }
    single<CouponRepository> { CouponRepositoryImpl(RetrofitInstance.couponApi) }
    single<CheckoutRepository> { CheckoutRepositoryImpl(RetrofitInstance.checkoutApi) }

    single<CoroutineScope>{ CoroutineScope(SupervisorJob()+ Dispatchers.IO) }
    single<SessionManager>{ SessionManager(get(),get(),get(),get(),get()) }
    single<SharedState> { SharedState() }

    single<UploadImageHelper>{ UploadImageHelper(get()) }
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ResetPasswordViewModel)
    viewModelOf(::AdminViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::BookDetailsViewModel)
    viewModelOf(::ChangePasswordViewModel)
    viewModelOf(::AddressViewModel)
    viewModelOf(::CouponViewModel)
    viewModelOf(::CheckoutViewModel)
}