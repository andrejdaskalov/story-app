package com.adaskalov.storyapp.domain

data class Story(
    val title: String,
    val messages: List<ChatMessage>
)
