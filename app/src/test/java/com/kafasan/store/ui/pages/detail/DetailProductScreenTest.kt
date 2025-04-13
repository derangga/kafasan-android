package com.kafasan.store.ui.pages.detail

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.compose.rememberNavController
import com.kafasan.store.data.Product
import com.kafasan.store.data.ProductEntity
import com.kafasan.store.domain.local.ProductRepository
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
    instrumentedPackages = ["androidx.loader.content"]
)
class DetailProductScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private val mockProductRepo = mockk<ProductRepository>()
    private val mockStoreRepo = mockk<StoreRepository>()

    private lateinit var viewModel: DetailProductViewModel

    @Before
    fun setup() {
        hiltRule.inject()

        viewModel = DetailProductViewModel(mockStoreRepo, mockProductRepo)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun detailScreen_showsAllSectionCorrectly() {
        coEvery {
            mockProductRepo.getProductById(any())
        } returns ProductEntity(1, "Product 1", "product-1", 100, "Desc 1", emptyList(), "", "")

        coEvery {
            mockStoreRepo.getProductById(any())
        } returns Result.Success(
            Product(
                id = 1,
                title = "Product 1",
                slug = "product-1",
                price = 100,
                description = "Desc 1",
                category = null,
                images = listOf(
                    "https://i.imgur.com/cHddUCu.jpeg",
                    "https://i.imgur.com/CFOjAgK.jpeg",
                    "https://i.imgur.com/wbIMMme.jpeg"
                ),
                creationAt = "",
                updatedAt = ""
            )
        )

        composeTestRule.setContent {
            val navController = rememberNavController()

            DetailProductScreen(navController,1, viewModel)
        }

        composeTestRule.onNodeWithTag("imageCarousel")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("title")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("price")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("description")
            .performScrollTo()
            .assertExists()
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Favorite")
            .assertIsDisplayed()
    }

    @Test
    fun detailScreen_showsUiUnFavoriteProduct() {
        coEvery {
            mockProductRepo.getProductById(any())
        } returns null

        coEvery {
            mockStoreRepo.getProductById(any())
        } returns Result.Success(
            Product(
                id = 1,
                title = "Product 1",
                slug = "product-1",
                price = 100,
                description = "Desc 1",
                category = null,
                images = listOf(
                    "https://i.imgur.com/cHddUCu.jpeg",
                    "https://i.imgur.com/CFOjAgK.jpeg",
                    "https://i.imgur.com/wbIMMme.jpeg"
                ),
                creationAt = "",
                updatedAt = ""
            )
        )

        composeTestRule.setContent {
            val navController = rememberNavController()
            val uiState = viewModel.product.collectAsState()

            DetailProductScreen(navController,1, viewModel)
        }

        composeTestRule.onNodeWithTag("imageCarousel")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("title")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("price")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("description")
            .performScrollTo()
            .assertExists()
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Favorite Border")
            .assertIsDisplayed()
    }

    @Test
    fun detailScreen_showsUiFailedState() {
        coEvery {
            mockProductRepo.getProductById(any())
        } returns null

        coEvery {
            mockStoreRepo.getProductById(any())
        } returns Result.Error(400, Exception("Bad request"))

        composeTestRule.setContent {
            val navController = rememberNavController()

            DetailProductScreen(navController,1, viewModel)
        }

        composeTestRule.onNodeWithTag("errorSection")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("imageCarousel")
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag("title")
            .assertDoesNotExist()
    }
}