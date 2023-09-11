package com.example.englishdiary.data.remote

import com.squareup.moshi.JsonClass
import org.json.JSONObject

@JsonClass(generateAdapter = true)
data class Message(
    val content: String?,
    val role: String?
) {
    companion object {
        fun extractJsonTextFromMarkdown(input: String): String? {
            // 与えられた文字列から```json```で囲まれた部分を正規表現で抽出
            val pattern = """```json\n(.*?)\n```""".toRegex(RegexOption.DOT_MATCHES_ALL)
            val matchResult = pattern.find(input.trimIndent())

            // マッチした部分がある場合、JSON文字列を取得
            val jsonStr = matchResult?.groupValues?.getOrNull(1)

            return jsonStr
        }

        fun mapToDiaryExampleJson(jsonText: String?): DiaryExample {
            val jsonObj = JSONObject(jsonText)
            return DiaryExample(content = jsonObj.optString("diary_example"))
        }

        fun extractDiaryExampleJsonResponse(input: String): DiaryExample {
            val jsonText = extractJsonTextFromMarkdown(input = input)
            return mapToDiaryExampleJson(jsonText = jsonText)
        }
    }
}