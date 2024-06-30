package com.adaskalov.storyapp.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adaskalov.storyapp.data.api.GenerativeApi
import com.adaskalov.storyapp.data.model.ModelResponse
import com.adaskalov.storyapp.domain.ChatMessage
import com.adaskalov.storyapp.domain.MessageAuthor
import com.adaskalov.storyapp.domain.Story
import com.adaskalov.storyapp.modules.StoryDatabase
import com.adaskalov.storyapp.repository.StoryRepository
import com.adaskalov.storyapp.repository.model.StoryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val generativeApi: GenerativeApi,
    private val storyRepository: StoryRepository
) : ViewModel() {


    private val chatText : MutableStateFlow<List<ChatMessage>> = MutableStateFlow(emptyList())
    val chatTextFlow: StateFlow<List<ChatMessage>> = chatText

    private val chatActions : MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val chatActionsFlow: StateFlow<List<String>> = chatActions

    private val chatTitle : MutableStateFlow<String> = MutableStateFlow("New story")
    val chatTitleFlow: StateFlow<String> = chatTitle

    private val uiState : MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val uiStateFlow: StateFlow<UiState> = uiState

    private var currentStoryId: Long = 0

    fun startChat(topic: String, setting: String, tone: String) {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            var response = ModelResponse("", emptyList())
            try {
                response = generativeApi.startChat(topic, setting, tone)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", e.stackTraceToString())
                uiState.value = UiState.Error("Error occurred with generation! ${e.stackTraceToString()}")
            }
            chatText.value += ChatMessage(response.response, MessageAuthor.MODEL)
            chatActions.value = response.actions
            chatTitle.value = "$topic in $setting in $tone style"
            uiState.value = UiState.Idle

            CoroutineScope(Dispatchers.IO).launch {
                currentStoryId = storyRepository.insertStory(Story(title = chatTitle.value, messages = chatText.value))
            }
        }
    }

     fun sendMessage(message: String) {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            chatActions.value = emptyList()
            chatText.value += ChatMessage(message, MessageAuthor.USER)

            CoroutineScope(Dispatchers.IO).launch {
                storyRepository.insertMessage(ChatMessage(message, MessageAuthor.USER), currentStoryId)
            }
            var response = ModelResponse("", emptyList())
            try {
                response = generativeApi.sendMessage(message)
            } catch (e: Exception) {
                Log.e("MainScreenViewModel", e.stackTraceToString())
                uiState.value = UiState.Error("Error occurred with generation! ${e.stackTraceToString()}")
            }
            chatText.value += ChatMessage(response.response, MessageAuthor.MODEL)
            chatActions.value = response.actions
            uiState.value = UiState.Idle

            CoroutineScope(Dispatchers.IO).launch {
                storyRepository.insertMessage(
                    ChatMessage(response.response, MessageAuthor.MODEL),
                    currentStoryId
                )
            }
        }

    }

    fun loadStory(storyId: Long) {
        currentStoryId = storyId
        CoroutineScope(Dispatchers.IO).launch {
            uiState.value = UiState.Loading
            val story = storyRepository.getStoryById(storyId)
            val actions = storyRepository.getStoryActions(storyId)
            chatText.value = story.messages
            chatTitle.value = story.title
            chatActions.value = actions
            uiState.value = UiState.Idle
        }
    }

    fun persistActions() {
        CoroutineScope(Dispatchers.IO).launch {
            storyRepository.updateStoryActions(currentStoryId, chatActions.value)
        }
    }

    
}

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data class Error(val error: String) : UiState()
}
