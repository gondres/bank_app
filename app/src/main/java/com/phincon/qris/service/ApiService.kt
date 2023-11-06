package com.phincon.qris.service

import com.phincon.qris.model.PromoResponse
import retrofit2.Response
import retrofit2.http.GET


interface ApiService {

    companion object {
        const val BASE_URL = "https://content.digi46.id/api/"
    }

    @GET("promos")
    suspend fun getPromos():Response<PromoResponse>

}