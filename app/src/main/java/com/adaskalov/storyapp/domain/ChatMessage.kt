package com.adaskalov.storyapp.domain

data class ChatMessage (
    val message: String,
    val messageBy: MessageAuthor
)

enum class MessageAuthor {
    USER,
    MODEL
}

fun String.toMessageAuthor() : MessageAuthor {
    return when(this) {
        "USER" -> MessageAuthor.USER
        "MODEL" -> MessageAuthor.MODEL
        else -> throw IllegalArgumentException("Unknown message author")
    }
}
