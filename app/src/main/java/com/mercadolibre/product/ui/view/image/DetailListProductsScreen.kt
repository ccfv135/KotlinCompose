package com.mercadolibre.product.ui.view.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.mercadolibre.component.LoadingBar
import com.mercadolibre.component.ShowToast
import com.mercadolibre.data.model.ProductItem
import com.mercadolibre.product.R
import com.mercadolibre.product.ui.model.ProductItemInfo
import com.mercadolibre.product.ui.theme.Shapes
import com.mercadolibre.product.ui.theme.Typography
import com.mercadolibre.product.ui.view.search.*
import com.mercadolibre.product.ui.view.search.Loading
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun DetailListProductsScreen(
    productItemTitle: String,
    onClick: (ProductItemInfo) -> Unit,
    productViewModel: ProductViewModel = hiltViewModel()
) {
    searchProducts(productViewModel, productItemTitle)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.detail_list)) },
                backgroundColor = Color.Yellow,
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        ProductListContent(vm = productViewModel, onImageClick = onClick)
    }
}

@Composable
private fun searchProducts(
    productViewModel: ProductViewModel,
    productItemTitle: String
) {
    LaunchedEffect(key1 = true, ){
        productViewModel.searchProducts(productItemTitle)
    }
}


@Composable
fun ProductListContent(vm: ProductViewModel, onImageClick: (ProductItemInfo) -> Unit) {
    vm.state.value.let { state ->
        when (state) {
            is Loading -> LoadingBar()
            is ProductListUiStateReady -> state.productList?.let {
                BindDetailList(
                    it,
                    onImageClick = onImageClick
                )
            }
            is ProductListUiStateError -> state.error?.let { ShowToast(it) }
        }
    }
}

@Composable
fun BindDetailList(list: List<ProductItem>, onImageClick: (ProductItemInfo) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)) {
        items(
            items = list,
            itemContent = { item ->
                ListDetailItem(item, onClick = { onClickedItem ->
                    onClickedItem.let {
                        val encodedUrl =
                            URLEncoder.encode(it.thumbnail, StandardCharsets.UTF_8.toString())

                        onImageClick.invoke(
                            ProductItemInfo(
                                encodedUrl,
                                it.title.replace("/", ""),
                                it.condition,
                                it.price.toString()
                            )
                        )
                    }
                })
            })
    }
}

@Composable
fun ListDetailItem(item: ProductItem, onClick: (ProductItem) -> Unit) {
    Card(
        shape = Shapes.large,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick(item) }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            CarImage(item)
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    style = Typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = item.condition,
                    style = Typography.caption
                )

                Text(
                    text =item.price.toString(),
                    style = Typography.body2
                )
            }
        }
    }
}

@Composable
fun CarImage(item: ProductItem) {
    Image(
        painter = rememberAsyncImagePainter(item.thumbnail),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .padding(end = 8.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
    )
}
