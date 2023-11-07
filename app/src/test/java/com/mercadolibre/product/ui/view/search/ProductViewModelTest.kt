package com.mercadolibre.product.ui.view.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mercadolibre.product.MainDispatcherRule
import com.mercadolibre.product.domain.usecase.getcars.GetProductItemUseCase
import com.mercadolibre.data.api.ApiService
import com.mercadolibre.data.model.MercadoLibreResponse
import com.mercadolibre.data.model.ProductItem
import com.mercadolibre.data.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*
import java.io.IOException

/**
 * Unit tests for the [ProductViewModel].
 */
@ExperimentalCoroutinesApi
class ProductViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Overrides Dispatchers.Main used in Coroutines
    @get:Rule
    var coroutineRule = MainDispatcherRule()

    private fun providesApiService(): ApiService = mock()

    private fun providesProductRepository(apiService: ApiService): ProductRepository =
        ProductRepository(apiService)

    private fun providesGetProductItemUseCase(repository: ProductRepository): GetProductItemUseCase =
        GetProductItemUseCase(repository, coroutineRule.testDispatcher)

    private fun providesProductViewModel(getProductItemUseCase: GetProductItemUseCase): ProductViewModel =
        ProductViewModel(getProductItemUseCase)

    @Test
    fun getProduct_isSuccess() = runTest {
        //Given
        val apiService = providesApiService()
        val repository = providesProductRepository(apiService)
        val getProductItemUseCase = providesGetProductItemUseCase(repository)
        val viewModel = providesProductViewModel(getProductItemUseCase)

        //When
        val mockProductList = listOf(
            ProductItem("Producto 1", "new", "http://url-to-thumbnail1.com", 10000),
            ProductItem("Producto 2", "used", "http://url-to-thumbnail2.com", 20000)
        )

        val mockResponse = MercadoLibreResponse(mockProductList)
        whenever(apiService.getProducts(any())).thenReturn(mockResponse)

        viewModel.searchProducts("query de ejemplo")
        advanceUntilIdle()

        //Then
        verify(apiService).getProducts("query de ejemplo")
        val currentState = viewModel.state.value
        assertTrue(currentState is ProductListUiStateReady && currentState.productList == mockProductList)
    }

    @Test
    fun getProducts_isFail() = runTest {
        //Given
        val apiService = providesApiService()
        val repository = providesProductRepository(apiService)
        val getProductItemUseCase = providesGetProductItemUseCase(repository)

        //When
        val viewModel = providesProductViewModel(getProductItemUseCase)
        whenever(repository.getProducts(any())) doAnswer {
            throw IOException()
        }
        viewModel.getProducts()

        //Then
        getProductItemUseCase().catch {
            assertEquals(ProductListUiStateError(), viewModel.state.value)
        }
    }
}