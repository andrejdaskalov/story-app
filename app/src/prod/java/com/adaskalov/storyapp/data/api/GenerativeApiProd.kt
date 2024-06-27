package com.adaskalov.storyapp.data.api

import com.adaskalov.storyapp.BuildConfig
import com.adaskalov.storyapp.data.model.ModelResponse
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import javax.inject.Inject


class GenerativeApiProd @Inject constructor() : GenerativeApi {
    private val apiKey = BuildConfig.apiKey
    private val systemInstruction = BuildConfig.systemPrompt
    private val generativeModel = GenerativeModel(
        apiKey = apiKey,
        modelName = "gemini-1.5-flash",
        systemInstruction = content{
            this.text(systemInstruction)
        },
        generationConfig = generationConfig {
            this.responseMimeType = "application/json"
        }
    )
    private val chat = generativeModel.startChat(history = emptyList())

    override suspend fun startChat(topic: String, setting: String, style: String): ModelResponse {
        chat.sendMessage("A story about $topic in $setting setting in $style style.").let {
            return ModelResponse.fromJson(it.text ?: "")
        }
    }

    override suspend fun sendMessage(message: String): ModelResponse {
        chat.sendMessage(message).let {
            return ModelResponse.fromJson(it.text ?: "")
        }
    }
}