package com.example.englishdiary.data.remote


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Usage(
    @Json(name = "completion_tokens")
    val completionTokens: Int?,
    @Json(name = "prompt_tokens")
    val promptTokens: Int?,
    @Json(name = "total_tokens")
    val totalTokens: Int?
)