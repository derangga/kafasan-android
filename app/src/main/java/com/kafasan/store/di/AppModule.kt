package com.kafasan.store.di

import android.content.Context
import androidx.room.Room
import com.kafasan.store.BuildConfig
import com.kafasan.store.domain.local.AppPreferences
import com.kafasan.store.domain.local.KafasanDB
import com.kafasan.store.domain.local.ProductDao
import com.kafasan.store.domain.local.ProductRepository
import com.kafasan.store.domain.local.ProductRepositoryImpl
import com.kafasan.store.domain.network.ApiUrl
import com.kafasan.store.domain.network.StoreRepository
import com.kafasan.store.domain.network.StoreRepositoryImpl
import com.kafasan.store.domain.network.StoreService
import com.kafasan.store.domain.network.TimerUtility
import com.kafasan.store.domain.network.TimerUtilityImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideRequestHeader(): OkHttpClient {
        val httpLoginInterceptor = HttpLoggingInterceptor()
        httpLoginInterceptor.level =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addNetworkInterceptor(httpLoginInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiUrl.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideStoreServices(retrofit: Retrofit): StoreService {
        return retrofit.create(StoreService::class.java)
    }

    @Provides
    @Singleton
    fun provideStoreRepository(service: StoreService): StoreRepository {
        return StoreRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideTheMovieDatabase(
        @ApplicationContext context: Context,
    ): KafasanDB {
        return Room.databaseBuilder(
            context,
            KafasanDB::class.java,
            "KafasanDB",
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideTrendingDao(db: KafasanDB): ProductDao {
        return db.productDao()
    }

    @Provides
    @Singleton
    fun provideProductRepository(dao: ProductDao): ProductRepository {
        return ProductRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideTimerUtility(): TimerUtility {
        return TimerUtilityImpl()
    }

    @Provides
    @Singleton
    fun provideAppPreferences(
        @ApplicationContext context: Context,
    ): AppPreferences {
        return AppPreferences(context)
    }
}
