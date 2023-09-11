package com.example.englishdiary.data.repository

import com.example.englishdiary.data.remote.OpenAiApi
import com.example.englishdiary.data.remote.OpenAiRequestDto
import com.example.englishdiary.data.remote.OpenAiResponseDto
import com.example.englishdiary.domain.repository.DiaryExampleRepository
import javax.inject.Inject

class DiaryExampleRepositoryImpl @Inject constructor(
    private val api: OpenAiApi
): DiaryExampleRepository {
    override suspend fun getDiaryExample(body: OpenAiRequestDto): OpenAiResponseDto {
        return api.getDiaryExample(body)
    }
}