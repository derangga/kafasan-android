package com.kafasan.store.domain.network

import com.google.gson.Gson
import com.kafasan.store.data.Product
import com.kafasan.store.utility.TestCoroutineRule
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class StoreRepositoryTest {
    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var storeService: StoreService
    private lateinit var storeRepository: StoreRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        storeService = generateRetrofit(mockWebServer)
        storeRepository = StoreRepositoryImpl(storeService)
    }

    @Test
    fun testGetProductsAndReturn200() {
        runTest {
            val products = generateProducts()
            val json = convertToJson(products)
            val res = MockResponse()
            res.setBody(json)
            res.setResponseCode(200)
            mockWebServer.enqueue(res)

            val result = storeRepository.getProducts()
            val productResponse = (result as? Result.Success)?.data.orEmpty()

            mockWebServer.takeRequest()

            Assert.assertTrue(result is Result.Success)
            Assert.assertEquals(products.size, productResponse.size)
        }
    }

    @Test
    fun testGetProductsAndReturn400() {
        runTest {
            val res = MockResponse()
            res.setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
            mockWebServer.enqueue(res)

            val result = storeRepository.getProducts()
            val status = (result as? Result.Error)?.status

            mockWebServer.takeRequest()

            Assert.assertTrue(result is Result.Error)
            Assert.assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, status)
        }
    }

    @Test
    fun testGetProductDetailAndReturn200() {
        runTest {
            val product = generateProducts(1).first()
            val json = convertToJson(product)
            val res = MockResponse()
            res.setBody(json)
            res.setResponseCode(200)
            mockWebServer.enqueue(res)

            val result = storeRepository.getProductById(1)
            val productResponse = (result as? Result.Success)?.data

            mockWebServer.takeRequest()

            Assert.assertTrue(result is Result.Success)
            Assert.assertEquals(product.id, productResponse?.id)
            Assert.assertEquals(product.title, productResponse?.title)
            Assert.assertEquals(product.description, productResponse?.description)
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun generateRetrofit(server: MockWebServer): StoreService {
        return Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(StoreService::class.java)
    }

    private fun generateProducts(size: Int = 5): List<Product> {
        return List(size) {
            val id: Long = it + 1L
            Product(id, "Product $id", "product-$id", 100, "Desc $id", null, emptyList(), "", "")
        }
    }

    private fun convertToJson(product: Any): String {
        val gson = Gson()
        return gson.toJson(product)
    }
}
