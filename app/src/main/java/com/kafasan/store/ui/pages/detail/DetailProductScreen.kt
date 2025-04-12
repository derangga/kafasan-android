package com.kafasan.store.ui.pages.detail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kafasan.store.data.Product
import com.kafasan.store.domain.network.ApiLoad
import com.kafasan.store.ui.components.CenterCircularLoading
import com.kafasan.store.ui.components.ImageCarousel

@Composable
fun DetailProductScreen(
    navController: NavHostController,
    id: Int,
    viewmodel: DetailProductViewModel
) {
    val state = viewmodel.product.collectAsStateWithLifecycle().value

    LaunchedEffect(id) {
        viewmodel.getProductById(id)
    }

    when (val apiLoad = state.product) {
        is ApiLoad.Error -> {
            DetailProductError(navController) {
                viewmodel.getProductById(id, true)
            }
        }

        ApiLoad.Loading -> {
            DetailProductLoading(navController)
        }

        is ApiLoad.Success -> {
            val product = apiLoad.data
            DetailProduct(navController, product, state.isFavorite) {
                viewmodel.favoriteProduct(product)
            }
        }
    }
}

@Composable
private fun DetailProductLoading(navController: NavHostController) {
    Column {
        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .background(Color(0x80F8F9FB), CircleShape)
                .padding(16.dp, 24.dp)
        ) {
            Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, contentDescription = "Back")
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CenterCircularLoading()
        }
    }
}

@Composable
private fun DetailProductError(navController: NavHostController, retry: () -> Unit) {
    Column {
        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .background(Color(0x80F8F9FB), CircleShape)
                .padding(16.dp, 24.dp)
        ) {
            Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, contentDescription = "Back")
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Text("Something wrong with your request, please try again")
                Button(
                    onClick = retry
                ) {
                    Text("Retry")
                }
            }
        }
    }
}

@Composable
private fun DetailProduct(
    navController: NavHostController,
    product: Product,
    isFavorite: Boolean,
    onFavorite: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Box {
            ImageCarousel(product.images)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.background(Color(0x80F8F9FB), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, contentDescription = "Back")
                }

                IconButton(
                    onClick = onFavorite,
                ) {
                    val icon = if (isFavorite) {
                        Icons.Default.Favorite
                    } else Icons.Default.FavoriteBorder
                    val color = if (isFavorite) {
                        Color(0xFFF44336)
                    } else Color.White
                    Icon(
                        imageVector = icon,
                        contentDescription = "Favorite",
                        tint = color
                    )
                }


            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Color(0xFFF8F9FB),
                        RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    product.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    "$${product.price}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Text("Details", modifier = Modifier.padding(top = 32.dp), fontSize = 18.sp)
                Text(
                    product.description,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(0xFF8891A5)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    val context = LocalContext.current
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Add To Cart", color = MaterialTheme.colorScheme.secondary)
                    }
                    Button(
                        onClick = {
                            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Buy Now")
                    }
                }
            }
        }
    }
}