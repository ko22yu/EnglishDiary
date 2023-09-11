package com.example.englishdiary.domain.repository

import com.example.englishdiary.data.remote.OpenAiRequestDto
import com.example.englishdiary.data.remote.OpenAiResponseDto

interface DiaryExampleRepository {

    suspend fun getDiaryExample(body: OpenAiRequestDto): OpenAiResponseDto
}