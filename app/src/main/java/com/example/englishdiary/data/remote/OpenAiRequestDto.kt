package com.example.englishdiary.data.remote


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenAiRequestDto(
    val messages: List<Message?>?,
    val model: String?,
    val temperature: Double?
)