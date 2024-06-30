package com.adaskalov.storyapp.ui.screens

import androidx.lifecycle.ViewModel
import com.adaskalov.storyapp.domain.Story
import com.adaskalov.storyapp.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val storyList: MutableStateFlow<List<Story>> = MutableStateFlow(emptyList())
    val storyListFlow : StateFlow<List<Story>> = storyList

    private val uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val uiStateFlow: StateFlow<UiState> = uiState

    fun getStories() {
        CoroutineScope(Dispatchers.IO).launch {
            uiState.value = UiState.Loading
            storyList.value = storyRepository.getStories()
            uiState.value = UiState.Idle
        }
    }


}