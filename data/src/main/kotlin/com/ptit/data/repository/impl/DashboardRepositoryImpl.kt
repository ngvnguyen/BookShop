package com.ptit.data.repository.impl

import com.ptit.data.RequestState
import com.ptit.data.api.DashboardApi
import com.ptit.data.model.ResponseEntity
import com.ptit.data.model.checkout.CheckoutData
import com.ptit.data.model.dashboard.OrderStatusCountData
import com.ptit.data.model.dashboard.OverviewData
import com.ptit.data.model.dashboard.RevenueByMonthData
import com.ptit.data.repository.DashboardRepository
import kotlinx.serialization.json.Json
import java.net.SocketTimeoutException

class DashboardRepositoryImpl(private val dashboardApi: DashboardApi): DashboardRepository {
    override suspend fun getOrderStatusCount(accessToken: String): RequestState<List<OrderStatusCountData>> {
        try{
            val response = dashboardApi.getOrderStatusCount("Bearer $accessToken")
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<List<OrderStatusCountData>>>(it).message
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

    override suspend fun getRevenueByMonth(accessToken: String): RequestState<List<RevenueByMonthData>> {
        try{
            val response = dashboardApi.getRevenueByMonth("Bearer $accessToken")
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<RevenueByMonthData>>(it).message
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

    override suspend fun getOverviewData(accessToken: String): RequestState<OverviewData> {
        try{
            val response = dashboardApi.getOverviewData("Bearer $accessToken")
            if (response.isSuccessful){
                val data = response.body()?.data
                data?.let { return RequestState.SUCCESS(it) }
            }
            val errBody = response.errorBody()?.string()
            errBody?.let {
                val errMsg = Json.decodeFromString<ResponseEntity<OverviewData>>(it).message
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