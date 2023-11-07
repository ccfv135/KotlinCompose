package com.mercadolibre.product

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mercadolibre.product.ui.navigation.NavGraph
import com.mercadolibre.product.ui.theme.MercadoLibreAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductAppMain()
        }
    }
}

@Composable
fun ProductAppMain() {
    MercadoLibreAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavGraph()
        }
    }
}