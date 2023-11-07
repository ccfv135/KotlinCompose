package com.mercadolibre.product.ui.model

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductItemInfo(
    val thumbnailUrl: String,
    val title: String,
    val condition: String,
    val price: String
):Parcelable

fun ProductItemInfo.toSafeArgsString(): String {
    return Gson().toJson(this)
}


fun String.toProductItemInfo(): ProductItemInfo? {
    return try {
        Gson().fromJson(this, ProductItemInfo::class.java)
    } catch (e: Exception) {
        null
    }
}
