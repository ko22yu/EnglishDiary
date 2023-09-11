package com.example.englishdiary.data.remote


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DiaryExample(
    val content: String?
) {
    companion object {
        fun diaryExampleFromJsonToModel(json: DiaryExample): com.example.englishdiary.domain.model.DiaryExample {
            return com.example.englishdiary.domain.model.DiaryExample(content = json.content ?: "")
        }
    }
}