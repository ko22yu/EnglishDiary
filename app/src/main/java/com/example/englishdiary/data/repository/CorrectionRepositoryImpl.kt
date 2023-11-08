package com.example.englishdiary.data.repository

import com.example.englishdiary.data.remote.Message
import com.example.englishdiary.data.remote.Message.Companion.makeOpenAiRequestDtoFromMessageList
import com.example.englishdiary.data.remote.OpenAiApi
import com.example.englishdiary.data.remote.OpenAiResponseDto
import com.example.englishdiary.domain.repository.CorrectionRepository
import javax.inject.Inject

class CorrectionRepositoryImpl @Inject constructor(
    private val api: OpenAiApi
) : CorrectionRepository {
    override suspend fun getTextResponse(messages: List<Message?>?): OpenAiResponseDto {
        return api.getTextResponse(makeOpenAiRequestDtoFromMessageList(messages))
    }
}