package com.example.englishdiary.data.remote

import com.squareup.moshi.JsonClass
import org.json.JSONArray
import org.json.JSONObject

@JsonClass(generateAdapter = true)
data class Message(
    val content: String?,
    val role: String?,
) {
    companion object {
        fun extractJsonTextFromMarkdown(input: String): String? {
            // 与えられた文字列から```json```で囲まれた部分を正規表現で抽出
            val pattern = """```json\n(.*?)\n```""".toRegex(RegexOption.DOT_MATCHES_ALL)
            var matchResult = pattern.find(input.trimIndent())

            var jsonStr = ""
            if (matchResult != null) {
                // マッチした部分がある場合、JSON文字列を取得
                jsonStr = matchResult?.groupValues?.getOrNull(1).toString()
            }

            if (matchResult == null) {
                val patternForCorrectionResult =
                    """\[\n(.*?)\n\]""".toRegex(RegexOption.DOT_MATCHES_ALL)
                val patternForDiaryExample = """\{([^{}]+)\}""".toRegex(RegexOption.DOT_MATCHES_ALL)
                if (patternForCorrectionResult.matches(input.trimIndent())) {
                    matchResult = patternForCorrectionResult.find(input.trimIndent())
                    // マッチした部分がある場合、JSON文字列を取得
                    jsonStr = "[" + matchResult?.groupValues?.getOrNull(1).toString() + "]"
                } else if (patternForDiaryExample.matches(input.trimIndent())) {
                    matchResult = patternForDiaryExample.find(input.trimIndent())
                    // マッチした部分がある場合、JSON文字列を取得
                    jsonStr = "{" + matchResult?.groupValues?.getOrNull(1).toString() + "}"
                } else {
                    throw IllegalArgumentException("APIの返答が正しい形式ではありません")
                }
            }

            return jsonStr
        }

        private fun mapToRemoteDiaryExample(jsonObj: JSONObject): DiaryExample {
            return DiaryExample(content = jsonObj.optString("diary_example"))
        }

        fun extractRemoteDiaryExample(input: String): DiaryExample {
            val jsonText = extractJsonTextFromMarkdown(input = input)
            val jsonObj = JSONObject(jsonText ?: "")
            return mapToRemoteDiaryExample(jsonObj = jsonObj)
        }

        private fun mapToCorrectionResult(jsonObj: JSONObject): CorrectionResult {
            return CorrectionResult(
                correctedEnText = jsonObj.optString("corrected_en_text"),
                jaText = jsonObj.optString("ja_text"),
                reasonForCorrection = jsonObj.optString("reason_for_correction")
            )
        }

        fun extractRemoteCorrectionResultList(input: String): List<CorrectionResult> {
            val jsonText = extractJsonTextFromMarkdown(input = input)
            val jsonArray = JSONArray(jsonText)

            val correctionResults = mutableListOf<CorrectionResult>()
            for (i in 0 until jsonArray.length()) {
                correctionResults.add(mapToCorrectionResult(jsonArray.getJSONObject(i)))
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