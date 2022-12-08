package com.example.bonialcodechallenge.di

import com.example.bonialcodechallenge.data.BonialService
import com.example.bonialcodechallenge.data.ContentDeserializer
import com.example.bonialcodechallenge.data.DefaultRemoteDataSource
import com.example.bonialcodechallenge.data.RemoteDataSource
import com.example.bonialcodechallenge.data.models.Content
import com.google.gson.GsonBuilder
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
object NetworkModule {

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
        val gson = GsonBuilder().registerTypeAdapter(Content::class.java, ContentDeserializer()).create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://test-mobile-configuration-files.s3.eu-central-1.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(BonialService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(bonialService: BonialService): RemoteDataSource = DefaultRemoteDataSource(bonialService)
}