package com.example.englishdiary.domain.usecase

import com.example.englishdiary.common.NetworkResponse
import com.example.englishdiary.data.remote.Message
import com.example.englishdiary.data.remote.toCorrectionResults
import com.example.englishdiary.data.remote.toDiaryExample
import com.example.englishdiary.domain.model.CorrectionResult
import com.example.englishdiary.domain.model.DiaryExample
import com.example.englishdiary.domain.repository.CorrectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CorrectionUseCase @Inject constructor(
    private val repository: CorrectionRepository
) {
    fun getDiaryExample(messages: List<Message?>?): Flow<NetworkResponse<DiaryExample>> = flow {
        try {
            emit(NetworkResponse.Loading())
            val diaryExample = repository.getTextResponse(messages).toDiaryExample()
            emit(NetworkResponse.Success(diaryExample))
        } catch (e: Exception) {
            emit(NetworkResponse.Failure(error = e.message.toString()))
        }
    }

    fun getCorrectionResults(messages: List<Message?>?): Flow<NetworkResponse<List<CorrectionResult>>> =
        flow {
            try {
                emit(NetworkResponse.Loading())
                val correctionResults = repository.getTextResponse(messages).toCorrectionResults()
                emit(NetworkResponse.Success(correctionResults))
            } catch (e: Exception) {
                emit(NetworkResponse.Failure(error = e.message.toString()))
            }
        }
}