package com.kafasan.store.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kafasan.store.data.Product

@Composable
fun ProductItem(product: Product) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            model = product.images.firstOrNull().orEmpty(),
            contentDescription = product.title,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = .4f))
        ) {
            Column(
                modifier = Modifier.padding(12.dp, 4.dp)
            ) {
                Text(
                    product.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text("Price: $${product.price}", fontSize = 12.sp, color = Color.White)
            }
        }

    }
}