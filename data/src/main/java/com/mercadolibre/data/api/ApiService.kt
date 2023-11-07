package com.mercadolibre.data.api

import com.mercadolibre.data.model.MercadoLibreResponse
import retrofit2.http.*

interface ApiService {

    @GET("sites/MCO/search")
    suspend fun getProducts(@Query("q") searchTerm: String): MercadoLibreResponse
}
