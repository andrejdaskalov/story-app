package com.adaskalov.storyapp.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adaskalov.storyapp.data.api.GenerativeApi
import com.adaskalov.storyapp.data.model.ModelResponse
import com.adaskalov.storyapp.domain.ChatMessage
import com.adaskalov.storyapp.domain.MessageAuthor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val generativeApi: GenerativeApi
) : ViewModel() {

    private val chatText : MutableStateFlow<List<ChatMessage>> = MutableStateFlow(emptyList())
    val chatTextFlow: StateFlow<List<ChatMessage>> = chatText

    private val chatActions : MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val chatActionsFlow: StateFlow<List<String>> = chatActions

    private val chatTitle : MutableStateFlow<String> = MutableStateFlow("New story")
    val chatTitleFlow: StateFlow<String> = chatTitle

    fun startChat(topic: String, setting: String, tone: String) {
        viewModelScope.launch {
            val response: ModelResponse = try {
                generativeApi.startChat(topic, setting, tone)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", e.stackTraceToString())
                ModelResponse("Error occurred with generation! ${e.stackTraceToString()}", emptyList())
            }
            chatText.value += ChatMessage(response.response, MessageAuthor.MODEL)
            chatActions.value = response.actions
            chatTitle.value = "$topic in $setting in $tone style"
        }
    }

     fun sendMessage(message: String) {
        viewModelScope.launch {
            chatActions.value = emptyList()
            chatText.value += ChatMessage(message, MessageAuthor.USER)
            val response = generativeApi.sendMessage(message)
            chatText.value += ChatMessage(response.response, MessageAuthor.MODEL)
            chatActions.value = response.actions
        }
    }

    
}