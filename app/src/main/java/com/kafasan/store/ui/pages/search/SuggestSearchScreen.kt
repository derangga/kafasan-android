package com.kafasan.store.ui.pages.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kafasan.store.ui.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestSearchScreen(navController: NavHostController, viewModel: SearchProductViewModel) {

    val focusRequester = remember { FocusRequester() }
    val state = viewModel.products.collectAsStateWithLifecycle()

    // Request focus when entering the screen
    LaunchedEffect("SearchProduct") {
        focusRequester.requestFocus()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(
                        hint = "samsung j-prime",
                        modifier = Modifier.focusRequester(focusRequester),
                        onSearchClicked = {
                            if (it.isNotEmpty()) {
                                navController.navigate("search/${it}")
                            }
                        },
                        onTextChange = {
                            viewModel.getSuggestion(it)
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            "back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { ip ->
        LazyColumn(
            modifier = Modifier.padding(ip)
        ) {
            items(state.value) { product ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        .clickable {
                            navController.navigate("product/${product.id}")
                        }
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "search",
                        tint = Color.Gray
                    )
                    Text(
                        product.title,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "search",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}