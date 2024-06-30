package com.adaskalov.storyapp.domain

data class Story(
    val id : Long? = null,
    val title: String,
    val messages: List<ChatMessage>
)
