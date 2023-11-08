package com.example.englishdiary.domain.repository

import com.example.englishdiary.data.remote.Message
import com.example.englishdiary.data.remote.OpenAiResponseDto

interface CorrectionRepository {

    suspend fun getTextResponse(messages: List<Message?>?): OpenAiResponseDto
}