package com.kafasan.store.ui.pages.favorite

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
import com.kafasan.store.data.ProductEntity
import com.kafasan.store.domain.local.ProductRepository
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
class FavoriteScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private val mockProductRepo = mockk<ProductRepository>()

    private lateinit var viewModel: FavoriteViewModel

    @Before
    fun setup() {
        hiltRule.inject()

        coEvery { mockProductRepo.getProducts() } returns
            listOf(
                ProductEntity(1, "Product 1", "product-1", 100, "Desc 1", emptyList(), "", ""),
                ProductEntity(2, "Product 2", "product-2", 150, "Desc 2", emptyList(), "", ""),
            )

        viewModel = FavoriteViewModel(mockProductRepo)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun favoriteScreen_showsFavoriteProducts() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            FavoriteScreen(navController, viewModel)
        }

        composeTestRule.onNodeWithText("Product 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Product 2").assertIsDisplayed()
    }

    @Test
    fun favoriteScreen_showsEmptyState() {
        coEvery { mockProductRepo.getProducts() } returns emptyList()

        composeTestRule.setContent {
            val navController = rememberNavController()

            FavoriteScreen(navController, viewModel)
        }

        composeTestRule
            .onNodeWithText("You don't have any favorite products")
            .assertIsDisplayed()
    }

    @Test
    fun favoriteScreen_navigatesToProductDetailOnCardClick() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val navController =
            TestNavHostController(context).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
                setGraph(
                    createGraph(startDestination = "favorite") {
                        composable("favorite") { }
                        composable("product/{id}") {
                            Text("Product Detail Screen")
                        }
                    },
                    startDestinationArgs = null,
                )
                setCurrentDestination("favorite")
            }

        // Mock product repo response
        coEvery { mockProductRepo.getProducts() } returns
            listOf(
                ProductEntity(1, "Product 1", "product-1", 100, "Desc 1", emptyList(), "", ""),
            )

        viewModel = FavoriteViewModel(mockProductRepo)

        composeTestRule.setContent {
            FavoriteScreen(navController, viewModel)
        }

        // Click the product card (you can also match by text)
        composeTestRule
            .onNodeWithText("Product 1")
            .performClick()

        // Assert navigation occurred
        assert(navController.currentBackStackEntry?.destination?.route?.startsWith("product/") == true)
    }
}
