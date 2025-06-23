package com.kafasan.store.ui.pages.search

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.requestFocus
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.kafasan.store.data.Product
import com.kafasan.store.domain.network.Result
import com.kafasan.store.domain.network.StoreRepository
import com.kafasan.store.domain.network.TimerUtility
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
class SuggestSearchScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private val mockStoreRepo = mockk<StoreRepository>()
    private val mockTimerUtility =
        object : TimerUtility {
            override fun debounceTime(): Long {
                return 0
            }
        }
    private lateinit var viewModel: SuggestSearchViewModel

    @Before
    fun setup() {
        hiltRule.inject()

        viewModel = SuggestSearchViewModel(mockTimerUtility, mockStoreRepo)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun searchScreen_showsProducts() {
        coEvery {
            mockStoreRepo.getProducts(title = any())
        } returns
            Result.Success(
                listOf(
                    Product(1, "Product 1", "product-1", 100, "Desc 1", null, emptyList(), "", ""),
                    Product(2, "Product 2", "product-2", 150, "Desc 2", null, emptyList(), "", ""),
                ),
            )
        val context = ApplicationProvider.getApplicationContext<Context>()
        val navController =
            TestNavHostController(context).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
                setGraph(
                    createGraph(startDestination = "suggest-search") {
                        composable("suggest-search") { }
                        composable("product/{id}") {
                            Text("Product Detail Screen")
                        }
                        composable("search/{query}") {
                            Text("Search Screen")
                        }
                    },
                    startDestinationArgs = null,
                )
                setCurrentDestination("suggest-search")
            }

        composeTestRule.setContent {
            SuggestSearchScreen(navController, viewModel)
        }

        composeTestRule.waitForIdle()

        // assert searchbar and perform typing
        composeTestRule.onNodeWithTag("searchBar")
            .assertExists()
            .assertIsDisplayed()
            .requestFocus()
            .assertIsFocused()
            .performTextInput("hello world")

        composeTestRule.onNodeWithText("hello world")
            .assertIsDisplayed()
            .requestFocus()
            .performImeAction()

        // assert goto search screen when tap ime search
        assert(navController.currentBackStackEntry?.destination?.route?.startsWith("search/") == true)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Product 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Product 2").assertIsDisplayed()
            .performClick()

        // assert to product detail
        assert(navController.currentBackStackEntry?.destination?.route?.startsWith("product/") == true)
    }
}
