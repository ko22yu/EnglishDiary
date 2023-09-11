package com.example.englishdiary.data.remote

import com.example.englishdiary.BuildConfig
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiApi {

    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer ${BuildConfig.OPENAI_API_KEY}"
    )
    @POST("completions")
    suspend fun getDiaryExample(
        @Body openAiRequestDto: OpenAiRequestDto
    ): OpenAiResponseDto
}