package com.kafasan.store.ui.pages.search

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.kafasan.store.data.Product
import com.kafasan.store.domain.network.Result
import com.kafasan.store.domain.network.StoreRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    application = HiltTestApplication::class,
    instrumentedPackages = ["androidx.loader.content"],
)
class SearchScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private val mockStoreRepo = mockk<StoreRepository>()
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        hiltRule.inject()

        coEvery { mockStoreRepo.getProducts(title = any()) } returns
            Result.Success(
                listOf(
                    Product(1, "Product 1", "product-1", 100, "Desc 1", null, emptyList(), "", ""),
                    Product(2, "Product 2", "product-2", 150, "Desc 2", null, emptyList(), "", ""),
                ),
            )

        viewModel = SearchViewModel(mockStoreRepo)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun searchScreen_showsProducts() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            SearchScreen(navController, "Shirt", viewModel)
        }

        composeTestRule.onNodeWithText("Product 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Product 2").assertIsDisplayed()
    }

    @Test
    fun searchScreen_navigatesToProductDetailOnCardClick() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val navController =
            TestNavHostController(context).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
                setGraph(
                    createGraph(startDestination = "search/{query}") {
                        composable("search/{query}") { }
                        composable("product/{id}") {
                            Text("Product Detail Screen")
                        }
                    },
                    startDestinationArgs = null,
                )
                setCurrentDestination("search/{query}")
            }

        // Mock product repo response
        coEvery { mockStoreRepo.getProducts() } returns
            Result.Success(
                listOf(
                    Product(1, "Product 1", "product-1", 100, "Desc 1", null, emptyList(), "", ""),
                ),
            )

        viewModel = SearchViewModel(mockStoreRepo)

        composeTestRule.setContent {
            SearchScreen(navController, "Shirt", viewModel)
        }

        // Click the product card (you can also match by text)
        composeTestRule
            .onNodeWithText("Product 1")
            .performClick()

        // Assert navigation occurred
        assert(navController.currentBackStackEntry?.destination?.route?.startsWith("product/") == true)
    }
}
