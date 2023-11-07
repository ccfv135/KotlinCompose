package com.mercadolibre.product.ui.navigation

import com.mercadolibre.product.ui.model.ProductItemInfo
import com.mercadolibre.product.ui.model.toSafeArgsString

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Images : Screen("images/{title}") {
        fun createRoute(title: String): String {
            return "images/$title"
        }
    }

    object Details : Screen("details/{productInfo}") {
        fun createRoute(productInfo: ProductItemInfo): String {
            val productInfoString = productInfo.toSafeArgsString()
            return "details/$productInfoString"
        }
    }
}