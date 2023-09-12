package com.example.englishdiary.common

object Constants {
    const val BASE_URL = "https://api.openai.com/v1/chat/"

    const val PROMPT_TO_GET_EXAMPLE_DIARY = """
        You have high writing skills.
        The output must be a markdown code snippet in Japanese formatted with the following schema:
        \`\`\`json
        {
           diary_example: string, // diary content.
        }
        \`\`\`
        
        Notes.
        * Diary content should be output around 100 characters.
        * Do not include anything other than JSON in your answer.
        * Answers must be in Japanese.
        
        I would like to write a diary. Please give me an example sentence.
    """
    const val PROMPT_FOR_CORRECTION = """
        
    """

    const val DEBUG_PROMPT_TO_GET_EXAMPLE_DIARY = """
        Please output the following.
        This is an answer.
        \`\`\`json
        {
            "diary_example": "こんにちは。"
        }
        \`\`\`
    """
    const val DEBUG_PROMPT_FOR_CORRECTION = """
        Please output the following.
        This is an answer.
        \`\`\`json
        [
            {
                "ja_text": "こんにちは。",
                "corrected_en_text": "Hello.",
                "reason_for_correction": "修正理由"
            },
            {
                "ja_text": "はじめまして。",
                "corrected_en_text": "Nice to meet you.",
                "reason_for_correction": "修正理由"
            }
        ]
        \`\`\`
    """
}