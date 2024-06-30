package com.adaskalov.storyapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adaskalov.storyapp.R
import com.adaskalov.storyapp.domain.ChatMessage
import com.adaskalov.storyapp.domain.MessageAuthor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    topic: String = "",
    setting: String = "",
    style: String = "",
    goBack: () -> Unit,
    storyId: Long? = null,
    speak: (String) -> Unit,
    stopSpeaking: () -> Unit,
    isSpeaking: () -> Boolean,

    ) {
    val viewModel: MainScreenViewModel = hiltViewModel()
    val chatList = viewModel.chatTextFlow.collectAsState()
    val actions = viewModel.chatActionsFlow.collectAsState()
    val chatTitle = viewModel.chatTitleFlow.collectAsState()
    val uiState = viewModel.uiStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        if (storyId != null) {
            viewModel.loadStory(storyId)
        } else {
            viewModel.startChat(topic, setting, style)
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(chatTitle.value) },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                navigationIcon = {
                    TextButton(onClick = {
                        viewModel.persistActions()
                        goBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
                 },
        bottomBar = {
            ChatActions(
                actions = actions.value,
                sendMessage = { message ->
                    viewModel.sendMessage(message)
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ChatContainer(chatList = chatList.value, isLoading = uiState.value == UiState.Loading, speak = speak, stopSpeaking = stopSpeaking, isSpeaking = isSpeaking)
        }
    }
}


@Composable
private fun ChatActions(
    actions: List<String>,
    sendMessage: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .navigationBarsPadding()
            .horizontalScroll(scrollState)
    ) {
        actions.forEach {
            Button(
                modifier = Modifier.padding(start = 16.dp),
                onClick = { sendMessage(it) }) {
                Text(it)
            }
        }
    }
}

@Composable
fun ChatContainer(chatList: List<ChatMessage>, isLoading: Boolean = false, speak: (String) -> Unit, stopSpeaking: () -> Unit, isSpeaking: () -> Boolean){
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = chatList, key2 = isLoading) {
        scrollState.animateScrollTo(Int.MAX_VALUE)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxHeight()
            .verticalScroll(scrollState)
    ) {

        chatList.forEach {
            if (it.messageBy == MessageAuthor.USER) {
                UserMessage(it)
            } else {
                ModelMessage(it, speak = speak, stopSpeaking = stopSpeaking, isSpeaking = isSpeaking)
            }
        }

        if (isLoading) {
            LoadingMessage()
        }

    }
}

@Composable
private fun ModelMessage(it: ChatMessage, speak: (String) -> Unit, stopSpeaking: () -> Unit, isSpeaking: () -> Boolean) {
    Text(
        text = it.message,
        modifier = Modifier
            .clickable {
                if (isSpeaking()) {
                    stopSpeaking()
                } else {
                    speak(it.message)
                }
            }
            .padding(vertical = 16.dp, horizontal = 8.dp)
    )
}

@Composable
private fun UserMessage(it: ChatMessage) {
    Box (
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
        .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.user_action, it.message),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .background(color = Color.Gray.copy(alpha = 0.2f))
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .fillMaxWidth()

        )
    }
}
@Composable
private fun LoadingMessage() {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        CircularProgressIndicator()
    }
}

