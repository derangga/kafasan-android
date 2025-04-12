package com.kafasan.store.ui.pages.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kafasan.store.data.Category
import com.kafasan.store.data.Product
import com.kafasan.store.ui.components.ProductItem

@Composable
fun OnBoardingScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        ) { ip ->
        Column(
            modifier = Modifier.padding(ip).verticalScroll(state = scrollState)
        ) {
            Button(
                onClick = {
                    navController.navigate("home")
                }
            ) {
                Text("Open Home")
            }
//            ProductCard(Product(
//                id = 1,
//                title = "Majestic Mountain Graphic T-Shirt",
//                slug = "majestic-mountain-graphic-t-shirt",
//                description = "",
//                category = Category(0, "", "", "", "", ""),
//                images = listOf(
//
//                        "https://i.imgur.com/QkIa5tT.jpeg",
//                        "https://i.imgur.com/jb5Yu0h.jpeg",
//                        "https://i.imgur.com/UlxxXyG.jpeg"
//
//                ),
//                creationAt = "",
//                updatedAt = "",
//                price = 10
//            ))
        }
    }
}