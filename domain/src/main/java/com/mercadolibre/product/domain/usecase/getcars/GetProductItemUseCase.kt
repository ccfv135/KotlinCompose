package com.mercadolibre.product.domain.usecase.getcars

import com.mercadolibre.data.common.Resource
import com.mercadolibre.data.model.ProductItem
import com.mercadolibre.data.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetProductItemUseCase @Inject constructor(
    private val repository: ProductRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): Flow<Resource<List<ProductItem>>> =
        getProductItemsFlow(DEFAULT_QUERY)

    suspend operator fun invoke(query: String): Flow<Resource<List<ProductItem>>> =
        getProductItemsFlow(query)

    private suspend fun getProductItemsFlow(query: String): Flow<Resource<List<ProductItem>>> {
        return flow {
            try {
                emit(Resource.loading())
                val products = query.let {
                    repository.getProducts(it).results
                }
                emit(Resource.success(products))
            } catch (e: Throwable) {
                emit(Resource.error(e))
            }
        }.flowOn(defaultDispatcher)
    }

    companion object {
        const val DEFAULT_QUERY = "Celulares"
    }
}
