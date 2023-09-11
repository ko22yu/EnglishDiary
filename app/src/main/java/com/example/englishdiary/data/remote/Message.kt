package com.example.englishdiary.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message(
    val content: String?,
    val role: String?
)