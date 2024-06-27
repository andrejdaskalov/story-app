package com.adaskalov.storyapp.data.api

import com.adaskalov.storyapp.data.model.ModelResponse

interface GenerativeApi {
    suspend fun startChat(topic: String, setting:String, style: String) : ModelResponse
    suspend fun sendMessage(message: String) : ModelResponse
}