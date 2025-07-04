package com.kafasan.store.ui.pages.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kafasan.store.ui.Route
import com.kafasan.store.ui.components.ProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    navController: NavHostController,
    viewModel: FavoriteViewModel,
) {
    val uiState = viewModel.products.collectAsStateWithLifecycle()

    LaunchedEffect("FavoriteScreen") {
        viewModel.getFavoriteProducts()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = { Text("Product Favorite", color = Color.White) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            "back",
                            tint = Color.White,
                        )
                    }
                },
            )
        },
    ) { ip ->
        LazyColumn(
            modifier =
                Modifier
                    .padding(ip),
        ) {
            val products = uiState.value
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (products.isEmpty()) {
                item {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "You don't have any favorite products",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            } else {
                items(products.size) { rowIndex ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        for (columnIndex in 0..1) {
                            val itemIndex = rowIndex * 2 + columnIndex
                            if (itemIndex > -1 && itemIndex < products.size) {
                                val product = products[itemIndex]
                                Card(
                                    modifier =
                                        Modifier
                                            .weight(1f)
                                            .height(220.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    onClick = {
                                        navController.navigate(
                                            Route.ProductDetail.createRoute(product.id),
                                        )
                                    },
                                ) {
                                    ProductItem(product)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
