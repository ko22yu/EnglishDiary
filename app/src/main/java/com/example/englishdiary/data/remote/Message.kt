package com.example.englishdiary.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message(
    val content: String?,
    val role: String?,
) {
    companion object {
        fun extractRemoteDiaryExampletFromMarkdown(input: String): DiaryExample {
            val regex = """\{\s*"diary_example":\s*"([^"]+)"\s*\}""".toRegex()
            val matchResult = regex.find(input.trimIndent())
                ?: throw IllegalArgumentException("APIの返答が正しい形式ではありません")
            val (diaryExample) = matchResult.destructured
            return DiaryExample(content = diaryExample)
        }

        fun extractRemoteCorrectionResultListFromMarkdown(input: String): List<CorrectionResult> {
            val regex =
                """\{\s+"ja_text":\s+"(.+?)",\s+"corrected_en_text":\s+"(.+?)",\s+"reason_for_correction":\s+"(.+?)"\s+\}""".toRegex()
            val matchResults = regex.findAll(input.trimIndent())
            if (matchResults == listOf<CorrectionResult>())
                throw IllegalArgumentException("APIの返答が正しい形式ではありません")
            val correctionResults = mutableListOf<CorrectionResult>()
            matchResults.forEach { matchResult ->
                val (jaText, correctedEnText, reasonForCorrection) = matchResult.destructured
                correctionResults.add(
                    CorrectionResult(
                        correctedEnText = correctedEnText,
                        jaText = jaText,
                        reasonForCorrection = reasonForCorrection
                    )
                )
            }
            return correctionResults
        }

        fun makeOpenAiRequestDtoFromMessageList(messageList: List<Message?>?): OpenAiRequestDto {
            return OpenAiRequestDto(
                messages = messageList,
                model = "gpt-3.5-turbo",
                temperature = 0.7,
            )
        }
    }
}