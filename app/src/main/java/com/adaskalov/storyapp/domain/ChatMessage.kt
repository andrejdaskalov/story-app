package com.adaskalov.storyapp.domain

data class ChatMessage (
    val message: String,
    val messageBy: MessageAuthor
)

enum class MessageAuthor {
    USER,
    MODEL
}
