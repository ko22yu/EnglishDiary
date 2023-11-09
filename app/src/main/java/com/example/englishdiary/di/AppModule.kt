package com.example.englishdiary.di

import com.example.englishdiary.common.Constants
import com.example.englishdiary.data.remote.OpenAiApi
import com.example.englishdiary.data.repository.CorrectionRepositoryImpl
import com.example.englishdiary.domain.repository.CorrectionRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOpenAiApi(): OpenAiApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // 接続のタイムアウトを設定
            .readTimeout(60, TimeUnit.SECONDS)    // 読み取りのタイムアウトを設定
            .writeTimeout(60, TimeUnit.SECONDS)   // 書き込みのタイムアウトを設定
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
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
    fun provideDiaryExampleRepository(api: OpenAiApi): CorrectionRepository {
        return CorrectionRepositoryImpl(api)
    }
}