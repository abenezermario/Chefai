package com.example.chefai

data class PostResponseData(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String
)