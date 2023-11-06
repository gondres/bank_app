package com.phincon.qris.service.repository

import com.phincon.qris.model.PromoResponse
import com.phincon.qris.service.ApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PromoRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getPromos(
    ): MainStateEvent<PromoResponse> {
        try {
            val response = apiService.getPromos()
            val data = response.body()
            if (response.isSuccessful) {
                if (data != null) {
                    return MainStateEvent.Success(data)
                }
            }
            return MainStateEvent.Success(data!!)
        } catch (e: HttpException) {
            val statusCode = e.code().toString()
            val statusMessage = e.message()
            return MainStateEvent.Error(statusCode, statusMessage)
        } catch (e: IOException) {
            return MainStateEvent.Exception(e.message ?: "No Internet Connection")
        } catch (e: Exception) {
            return MainStateEvent.Exception(e.message ?: "Unknown error")
        }
    }
}