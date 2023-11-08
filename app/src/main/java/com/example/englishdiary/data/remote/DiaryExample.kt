package com.example.englishdiary.data.remote


import com.squareup.moshi.JsonClass
import com.example.englishdiary.domain.model.DiaryExample as modelDiaryExample

@JsonClass(generateAdapter = true)
data class DiaryExample(
    val content: String?
) {
    companion object {
        fun diaryExampleFromRemoteToModel(remoteDiaryExample: DiaryExample): modelDiaryExample {
            return modelDiaryExample(content = remoteDiaryExample.content ?: "")
        }
    }
}