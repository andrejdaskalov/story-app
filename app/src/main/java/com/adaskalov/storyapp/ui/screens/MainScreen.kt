package com.adaskalov.storyapp.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.adaskalov.storyapp.R
import com.adaskalov.storyapp.domain.ChatMessage
import com.adaskalov.storyapp.domain.MessageAuthor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel: MainScreenViewModel = hiltViewModel()
    val chatList = viewModel.chatTextFlow.collectAsState()
    val actions = viewModel.chatActionsFlow.collectAsState()
    val chatTitle = viewModel.chatTitleFlow.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(chatTitle.value) },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
                 },
        bottomBar = {
            ChatActions(
                actions = actions.value,
                startChat = { topic, setting, tone ->
                    viewModel.startChat(topic, setting, tone)
                },
                sendMessage = { message ->
                    viewModel.sendMessage(message)
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ChatContainer(chatList = chatList.value)
        }
    }
}


@Composable
private fun ChatActions(
    actions: List<String>,
    startChat: (String, String, String) -> Unit,
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
        Button(
            modifier = Modifier.padding(start = 16.dp),
            onClick = {
            startChat(
                "cowboy cyberpunk",
                "22nd century russia",
                "comedy"
            )
        }) {
            Text("Start Chat")
        }
    }
}

@Composable
fun ChatContainer(chatList: List<ChatMessage>) {
    val scrollState = rememberScrollState()
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
                ModelMessage(it)
            }
        }

    }
}

@Composable
private fun ModelMessage(it: ChatMessage) {
    Text(
        text = it.message,
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
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


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}