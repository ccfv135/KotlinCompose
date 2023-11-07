package com.mercadolibre.product.ui.view.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadolibre.product.domain.usecase.getcars.GetProductItemUseCase
import com.mercadolibre.data.common.Resource
import com.mercadolibre.data.model.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productItemUseCase: GetProductItemUseCase,
) : ViewModel() {

    val state = mutableStateOf<ProductListUiState>(Loading)
    private var _query: String = ""

    fun searchProducts(query: String) = viewModelScope.launch {
        _query = query
        productItemUseCase(_query).collect {
            handleResponse(it)
        }
    }

    fun getProducts() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            productItemUseCase().collect(::handleResponse)
        }
    }

    private suspend fun handleResponse(it: Resource<List<ProductItem>>) = withContext(Dispatchers.Main) {
        when (it.status) {
            Resource.Status.LOADING -> state.value = Loading
            Resource.Status.SUCCESS -> state.value = ProductListUiStateReady(productList = it.data)
            Resource.Status.ERROR -> state.value =
                ProductListUiStateError(error = it.error?.data?.message)
        }
    }
}

sealed class ProductListUiState
data class ProductListUiStateReady(val productList: List<ProductItem>?) : ProductListUiState()
object Loading : ProductListUiState()
class ProductListUiStateError(val error: String? = null) : ProductListUiState()