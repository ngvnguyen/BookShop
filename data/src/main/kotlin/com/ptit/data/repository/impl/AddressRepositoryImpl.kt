package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.AddressApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.address.AddressData
import com.ptit.data.model.address.AddressRequestForm
import com.ptit.data.model.address.AllAddressResponse
import com.ptit.data.model.author.fetch.FetchAllAuthorResponse
import com.ptit.data.repository.AddressRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class AddressRepositoryImpl(private val addressApi: AddressApi): AddressRepository {
    override suspend fun createNewAddress(
        accessToken: String,
        createAddressRequest: AddressRequestForm
    ): RequestState<String> {
        try {
            val response = addressApi.createNewAddress("Bearer $accessToken",createAddressRequest)
            if (response.isSuccessful){
                val message = response.body()?.message
                return RequestState.SUCCESS(message?:"Address created successfully")
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<AddressData>>(it).message
                return RequestState.ERROR(errMsg)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun updateAddress(
        accessToken: String,
        id: Int,
        updateAddressRequest: AddressRequestForm
    ): RequestState<String> {
        try {
            val response = addressApi.updateAddressById("Bearer $accessToken",id,updateAddressRequest)
            if (response.isSuccessful){
                val message = response.body()?.message
                return RequestState.SUCCESS(message?:"Address updated successfully")
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<AddressData>>(it).message
                return RequestState.ERROR(errMsg)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun deleteAddress(
        accessToken: String,
        id: Int
    ): RequestState<String> {
        try {
            val response = addressApi.deleteAddressById("Bearer $accessToken",id)
            if (response.isSuccessful){
                return RequestState.SUCCESS("Address deleted successfully")
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<Unit>>(it).message
                return RequestState.ERROR(errMsg)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun getAllAddress(accessToken: String): RequestState<List<AddressData>> {
        try {
            val response = addressApi.getAllAddress("Bearer $accessToken")
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it.addresses) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<AllAddressResponse>>(it).message
                return RequestState.ERROR(errMsg)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun getAddressById(
        accessToken: String,
        id: Int
    ): RequestState<AddressData> {
        try {
            val response = addressApi.getAddressById("Bearer $accessToken",id)
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<AddressData>>(it).message
                return RequestState.ERROR(errMsg)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }

    override suspend fun getDefaultAddress(accessToken: String): RequestState<AddressData> {
        try {
            val response = addressApi.getAddressById("Bearer $accessToken",1)
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<AddressData>>(it).message
                return RequestState.ERROR(errMsg)
            }
            return RequestState.ERROR("Unknown error")
        }catch (e: SocketTimeoutException){
            return RequestState.ERROR("Server not responding")
        } catch (e: Exception){
            e.printStackTrace()
            return RequestState.ERROR("Network error")
        }
    }
}