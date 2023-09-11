package com.example.englishdiary.di

import com.example.englishdiary.common.Constants.BASE_URL
import com.example.englishdiary.data.remote.OpenAiApi
import com.example.englishdiary.data.repository.DiaryExampleRepositoryImpl
import com.example.englishdiary.domain.repository.DiaryExampleRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOpenAiApi(): OpenAiApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
            .create(OpenAiApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDiaryExampleRepository(api: OpenAiApi): DiaryExampleRepository {
        return DiaryExampleRepositoryImpl(api)
    }
}