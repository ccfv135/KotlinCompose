package com.mercadolibre.data.model

import com.google.gson.annotations.SerializedName
data class MercadoLibreResponse(
    @SerializedName("results")
    val results: List<ProductItem>
)
data class ProductItem(
    @SerializedName("title")
    val title: String,
    @SerializedName("condition")
    val condition: String,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("price")
    val price: Int
)
