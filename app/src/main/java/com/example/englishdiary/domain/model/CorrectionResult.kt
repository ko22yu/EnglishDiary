package com.example.englishdiary.domain.model

import com.squareup.moshi.Json

data class CorrectionResult(
    @Json(name = "corrected_en_text")
    val correctedEnText: String?,
    @Json(name = "ja_text")
    val jaText: String?,
    @Json(name = "reason_for_correction")
    val reasonForCorrection: String?
)
