package com.example.englishdiary.common

object Constants {
    const val BASE_URL = "https://api.openai.com/v1/chat/"

    const val PROMPT_TO_GET_EXAMPLE_DIARY = "You have high writing skills.\n" +
            "The output must be a markdown code snippet in Japanese formatted with the following schema:\n" +
            "\\`\\`\\`json\n" +
            "{\n" +
            "   diary_example: string, // diary content.\n" +
            "}\n" +
            "\\`\\`\\`\n" +
            "\n" +
            "Notes.\n" +
            "* Diary content should be output around 100 characters.\n" +
            "* Do not include anything other than JSON in your answer.\n" +
            "* Answers must be in Japanese.\n" +
            "\n" +
            "I would like to write a diary. Please give me an example sentence."

    const val DEBUG = """
        Please output the following.
        \`\`\`json
        {
            "diary_example": "こんにちは。"
        }
        \`\`\`
    """
}