package com.mercadolibre.product.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mercadolibre.product.ui.model.ProductItemInfo
import com.mercadolibre.product.ui.model.toProductItemInfo
import com.mercadolibre.product.ui.view.detailsinfo.DetailInfoScreen
import com.mercadolibre.product.ui.view.image.DetailListProductsScreen
import com.mercadolibre.product.ui.view.search.SearchScreen

@Preview
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }

    NavHost(navController, startDestination = Screen.Search.route) {
        composable(Screen.Search.route) { SearchScreen { actions.goToImagesScreen(it.title) } }
        composable(Screen.Images.route) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")
            val actions = remember(navController) { MainActions(navController) }


            val navigateWithProductTitle: (ProductItemInfo) -> Unit = { productItemInfo ->
                actions.goToDetailsScreen(productItemInfo)
            }

            if (title != null) {
                DetailListProductsScreen(
                    productItemTitle = title,
                    onClick = navigateWithProductTitle
                )
            }
        }


        composable(Screen.Details.route) { backStackEntry ->
            val productInfoString = backStackEntry.arguments?.getString("productInfo")
            val productItemInfo = productInfoString?.toProductItemInfo()
            if (productItemInfo != null) {
                DetailInfoScreen(productItemInfo)
            }
        }
    }
}

class MainActions(private val navController: NavHostController) {

    val goToImagesScreen: (String) -> Unit = { title ->
        navController.navigate(Screen.Images.createRoute(title))
    }

    val goToDetailsScreen: (ProductItemInfo) -> Unit = { productItemInfo ->
        navController.navigate(Screen.Details.createRoute(productItemInfo))
    }
}