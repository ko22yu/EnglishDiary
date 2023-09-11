package com.example.englishdiary.domain.usecase

import com.example.englishdiary.common.NetworkResponse
import com.example.englishdiary.data.remote.OpenAiRequestDto
import com.example.englishdiary.data.remote.toDiaryExample
import com.example.englishdiary.domain.model.DiaryExample
import com.example.englishdiary.domain.repository.CorrectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class CorrectionUseCase @Inject constructor(
    private val repository: CorrectionRepository
) {
    operator fun invoke(body: OpenAiRequestDto): Flow<NetworkResponse<DiaryExample>> = flow {
        try {
            emit(NetworkResponse.Loading<DiaryExample>())
            val diaryExample = repository.getDiaryExample(body).toDiaryExample()
            emit(NetworkResponse.Success<DiaryExample>(diaryExample))
        } catch (e: Exception) {
            emit(NetworkResponse.Failure<DiaryExample>(error = e.message.toString()))
        }
    }
}