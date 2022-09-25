package com.eone.submission2_bpai.di

import com.eone.submission2_bpai.network.ApiConfig
import com.eone.submission2_bpai.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    /**
     * Provide API Service instance for Hilt
     *
     * @return ApiService
     */
    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiConfig.apiInstance
}