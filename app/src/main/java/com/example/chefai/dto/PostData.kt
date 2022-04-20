package com.example.chefai

data class PostData(
//    val logprobs: Any,
    val max_tokens: Int,
//    val n: Int,
    val prompt: String,
//    val stop: String,
//    val stream: Boolean,
    val temperature: Double,
    val top_p: Double,
    val presence_penalty: Double,
)