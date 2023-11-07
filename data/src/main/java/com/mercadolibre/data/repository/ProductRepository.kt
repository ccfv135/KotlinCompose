package com.mercadolibre.data.repository

import com.mercadolibre.data.api.ApiService
import com.mercadolibre.data.model.MercadoLibreResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped

import javax.inject.Inject

@ActivityRetainedScoped
class ProductRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getProducts(query: String): MercadoLibreResponse = apiService.getProducts(query)
}