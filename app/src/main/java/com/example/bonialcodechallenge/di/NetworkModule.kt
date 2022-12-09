package com.example.bonialcodechallenge.di

import com.example.bonialcodechallenge.data.BrochureRepository
import com.example.bonialcodechallenge.data.BrochureRepositoryImpl
import com.example.bonialcodechallenge.data.local.BrochureLocalDataSource
import com.example.bonialcodechallenge.data.local.BrochureLocalDataSourceImpl
import com.example.bonialcodechallenge.data.models.Content
import com.example.bonialcodechallenge.data.remote.BonialService
import com.example.bonialcodechallenge.data.remote.BrochureRemoteDataSource
import com.example.bonialcodechallenge.data.remote.BrochureRemoteDataSourceImpl
import com.example.bonialcodechallenge.data.remote.ContentDeserializer
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    companion object {

        @Provides
        @Singleton
        fun provideOkHttpClient(
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .build()
        }

        @Provides
        @Singleton
        fun provideBonialService(): BonialService {
            val gson = GsonBuilder().registerTypeAdapter(Content::class.java, ContentDeserializer())
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://test-mobile-configuration-files.s3.eu-central-1.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(BonialService::class.java)
        }

    }

    @Singleton
    @Binds
    abstract fun provideRepository(
        impl: BrochureRepositoryImpl
    ): BrochureRepository

    @Singleton
    @Binds
    abstract fun bindBrochureLocalDataSource(
        impl: BrochureLocalDataSourceImpl
    ): BrochureLocalDataSource

    @Singleton
    @Binds
    abstract fun bindBrochureRemoteDataSource(
        impl: BrochureRemoteDataSourceImpl
    ): BrochureRemoteDataSource

}