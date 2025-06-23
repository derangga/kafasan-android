package com.kafasan.store.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ImageCarousel(
    items: List<String>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { items.size }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier.height(300.dp),
        ) {
            HorizontalPager(
                state = pagerState,
            ) { idx ->
                CarouselItem(items[idx], pagerState, items.size)
            }
        }
    }
}

@Composable
fun CarouselIndicator(
    state: PagerState,
    size: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(size) {
            Spacer(
                modifier =
                    Modifier.padding(4.dp).width(20.dp).height(4.dp)
                        .background(
                            color =
                                if (state.currentPage == it) {
                                    MaterialTheme.colorScheme.tertiary
                                } else {
                                    MaterialTheme.colorScheme.primary
                                },
                            shape = RoundedCornerShape(8.dp),
                        ),
            )
        }
    }
}

@Composable
fun CarouselItem(
    image: String,
    state: PagerState,
    size: Int,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize(),
    ) {
        AsyncImage(
            model = image,
            contentDescription = "backdrop",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 16.dp),
        ) {
            CarouselIndicator(state, size)
        }
    }
}
