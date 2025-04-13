package com.kafasan.store.domain.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kafasan.store.data.ProductEntity
import com.kafasan.store.utility.TestCoroutineRule
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ProductRepositoryTest {
    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private lateinit var productDao: ProductDao
    private lateinit var db: KafasanDB
    private lateinit var productRepo: ProductRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, KafasanDB::class.java).build()
        productDao = db.productDao()
        productRepo = ProductRepositoryImpl(productDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertProductShouldStoreData() = runTest {
        // when
        productRepo.insert(generateProduct())

        // then
        val product = productRepo.getProductById(1)
        Assert.assertEquals(1L, product?.id)
        Assert.assertEquals("Product 1", product?.title)
        Assert.assertEquals("product-1", product?.slug)
        Assert.assertEquals("Desc 1", product?.description)
    }

    @Test
    fun deleteProductShouldDeleteItemCorrectly() = runTest {
        // when
        val products = generateProducts()
        products.forEach {
            productDao.insert(it)
        }

        // then
        val productsInDb = productDao.getProducts()
        Assert.assertEquals(5, productsInDb.size)

        // when
        productDao.delete(3)

        // then
        val afterDeleteProducts = productDao.getProducts()
        Assert.assertEquals(4, afterDeleteProducts.size)
        val deletedProduct = afterDeleteProducts.find { it.id == 3L }
        Assert.assertNull(deletedProduct)
    }

    private fun generateProduct(): ProductEntity {
        return ProductEntity(1, "Product 1", "product-1", 100, "Desc 1", emptyList(), "", "")
    }

    private fun generateProducts(size: Int = 5): List<ProductEntity> {
        return List(size) {
            val id: Long = it + 1L
            ProductEntity(id, "Product $id", "product-$id", 100, "Desc $id", emptyList(), "", "")
        }
    }
}