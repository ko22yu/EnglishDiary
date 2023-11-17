package com.example.englishdiary.data.remote


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.example.englishdiary.domain.model.CorrectionResult as modelCorrectionResult

@JsonClass(generateAdapter = true)
data class CorrectionResult(
    @Json(name = "corrected_en_text")
    val correctedEnText: String?,
    @Json(name = "ja_text")
    val jaText: String?,
    @Json(name = "reason_for_correction")
    val reasonForCorrection: String?
) {
    companion object {
        fun correctionResultListFromRemoteToModel(
            remoteCorrectionResultList: List<CorrectionResult>
        ): List<modelCorrectionResult> {
            return remoteCorrectionResultList.map {
                modelCorrectionResult(
                    enText = "",
                    correctedEnText = it.correctedEnText,
                    jaText = it.jaText,
                    reasonForCorrection = it.reasonForCorrection
                )
            }
        }
    }
}