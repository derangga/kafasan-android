package com.kafasan.store.ui.pages.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kafasan.store.ui.Route
import com.kafasan.store.ui.components.CenterCircularLoading
import com.kafasan.store.ui.components.ProductItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
) {
    val pagingItems = viewModel.getProducts().collectAsLazyPagingItems()
    val listState = rememberLazyListState()
    val collapsed by remember {
        derivedStateOf {
            val firstVisibleItem = listState.firstVisibleItemIndex
            val offset = listState.firstVisibleItemScrollOffset
            firstVisibleItem > 0 || offset > 20
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
    ) {
        stickyHeader {
            val animatedHeight by animateDpAsState(
                targetValue = if (collapsed) 70.dp else 120.dp,
                animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
                label = "AnimatedHeaderHeight",
            )
            CollapsibleHeader(
                height = animatedHeight,
                collapsed = collapsed,
                onFavoriteClick = {
                    navController.navigate(Route.Favorite.route)
                },
                onSearchClick = {
                    navController.navigate(Route.SuggestSearch.route)
                },
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(pagingItems.itemCount) { rowIndex ->
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                for (columnIndex in 0..1) {
                    val itemIndex = rowIndex * 2 + columnIndex
                    if (itemIndex > -1 && itemIndex < pagingItems.itemCount) {
                        val product = pagingItems[itemIndex]
                        product?.also { prd ->
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
                                ProductItem(prd)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        when (pagingItems.loadState.source.refresh) {
            is LoadState.Error -> {
                item {
                    Box {
                        Toast.makeText(
                            LocalContext.current,
                            "Failed get product",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }

            else -> {
                item {
                    CenterCircularLoading()
                }
            }
        }
    }
}

@Composable
fun CollapsibleHeader(
    height: Dp,
    collapsed: Boolean,
    onFavoriteClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(height),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 4.dp,
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            AnimatedVisibility(
                visible = !collapsed,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Welcome", color = Color.White, fontSize = 20.sp)
                        FavoriteIcon(onFavoriteClick)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    SearchBar(onClick = onSearchClick)
                }
            }
            AnimatedVisibility(
                visible = collapsed,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SearchBar(modifier = Modifier.weight(1f), onClick = onSearchClick)
                    Spacer(modifier = Modifier.width(12.dp))
                    FavoriteIcon(onFavoriteClick)
                }
            }
        }
    }
}

@Composable
fun FavoriteIcon(onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Default.Favorite,
        contentDescription = "Favorite",
        tint = Color.White,
        modifier = Modifier.clickable(onClick = onClick),
    )
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(30.dp))
                .padding(horizontal = 16.dp)
                .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Search Products or store",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
        )
    }
}
