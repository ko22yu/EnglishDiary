package com.example.englishdiary.data.remote


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
    return DiaryExample(choices!!.get(0)!!.message!!.content!!)
}