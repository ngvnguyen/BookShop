package com.ptit.data.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ptit.data.BuildConfig
import com.ptit.data.api.AddressApi
import com.ptit.data.api.AuthApi
import com.ptit.data.api.AuthorApi
import com.ptit.data.api.BookApi
import com.ptit.data.api.CartApi
import com.ptit.data.api.CategoryApi
import com.ptit.data.api.CheckoutApi
import com.ptit.data.api.CouponApi
import com.ptit.data.api.FileApi
import com.ptit.data.api.PermissionApi
import com.ptit.data.api.PublisherApi
import com.ptit.data.api.RoleApi
import com.ptit.data.api.UserApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.getValue

object RetrofitInstance {
    private const val BaseUrl = BuildConfig.BASE_URL
    private val interceptor = HttpLoggingInterceptor().apply{
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    private val json = Json{
        ignoreUnknownKeys = true
    }
    private val retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(BaseUrl)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val authApi by lazy {
        retrofit.create(AuthApi::class.java)
    }
    val authorApi by lazy {
        retrofit.create(AuthorApi::class.java)
    }
    val bookApi by lazy {
        retrofit.create(BookApi::class.java)
    }
    val categoryApi by lazy {
        retrofit.create(CategoryApi::class.java)
    }
    val permissionApi by lazy {
        retrofit.create(PermissionApi::class.java)
    }
    val publisherApi by lazy {
        retrofit.create(PublisherApi::class.java)
    }
    val roleApi by lazy {
        retrofit.create(RoleApi::class.java)
    }
    val userApi by lazy {
        retrofit.create(UserApi::class.java)
    }
    val fileApi by lazy {
        retrofit.create(FileApi::class.java)
    }
    val cartApi by lazy{
        retrofit.create(CartApi::class.java)
    }
    val addressApi by lazy{
        retrofit.create(AddressApi::class.java)
    }
    val couponApi by lazy{
        retrofit.create(CouponApi::class.java)
    }
    val checkoutApi by lazy{
        retrofit.create(CheckoutApi::class.java)
    }
}