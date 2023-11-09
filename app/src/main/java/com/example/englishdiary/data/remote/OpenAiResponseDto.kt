package com.example.englishdiary.data.remote


import com.example.englishdiary.data.remote.CorrectionResult.Companion.correctionResultListFromRemoteToModel
import com.example.englishdiary.data.remote.DiaryExample.Companion.diaryExampleFromRemoteToModel
import com.example.englishdiary.data.remote.Message.Companion.extractRemoteCorrectionResultListFromMarkdown
import com.example.englishdiary.data.remote.Message.Companion.extractRemoteDiaryExampletFromMarkdown
import com.example.englishdiary.domain.model.CorrectionResult
import com.example.englishdiary.domain.model.DiaryExample
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenAiResponseDto(
    val choices: List<Choice?>?,
    val created: Int?,
    val id: String?,
    val model: String?,
    @Json(name = "object")
    val objectX: String?,
    val usage: Usage?
)

fun OpenAiResponseDto.toDiaryExample(): DiaryExample {
    return diaryExampleFromRemoteToModel(
        extractRemoteDiaryExampletFromMarkdown(choices?.get(0)?.message?.content ?: "")
    )
}

fun OpenAiResponseDto.toCorrectionResults(): List<CorrectionResult> {
    return correctionResultListFromRemoteToModel(
        extractRemoteCorrectionResultListFromMarkdown(choices?.get(0)?.message?.content ?: "")
    )
}