package com.kafasan.store.domain.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kafasan.store.data.Converters
import com.kafasan.store.data.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class KafasanDB: RoomDatabase() {
    abstract fun productDao(): ProductDao
}