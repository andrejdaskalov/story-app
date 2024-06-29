package com.adaskalov.storyapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.adaskalov.storyapp.MainScreenDestination
import com.adaskalov.storyapp.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun NewChatScreen(
    navigateToMainScreen: (topic: String, setting: String, style: String) -> Unit
) {
    val topicText = remember { mutableStateOf("") }
    val settingText = remember { mutableStateOf("") }
    val styleText = remember { mutableStateOf("") }


    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(id = R.drawable.undraw_book_lover_re_rwjy),
                    contentDescription = "StoryApp Logo",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
//                        .width(100.dp)
                )
                Spacer(modifier = Modifier.height(55.dp))

                SceneChooser(topicText, settingText, styleText)

                Spacer(modifier = Modifier.height(50.dp))
                ElevatedButton(
                    onClick = {
                        navigateToMainScreen(topicText.value, settingText.value, styleText.value)
                    }) {
                    BodyText(value = "✍️ Craft Story")
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun SceneChooser(
    topicText: MutableState<String>,
    settingText: MutableState<String>,
    styleText: MutableState<String>
) {
    FlowRow(
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        BodyText(value = stringResource(R.string.i_want_a_story_about))
        InputTextField(
            value = "",
            onValueChange = {
                topicText.value = it
            },
        )
        BodyText(value = stringResource(R.string.in_setting))
        InputTextField(
            value = "",
            onValueChange = {
                settingText.value = it
            },
        )
        BodyText(value = stringResource(R.string.in_setting))
        InputTextField(
            value = "",
            onValueChange = {
                styleText.value = it
            },
        )
        BodyText(value = stringResource(R.string.style))
    }
}

@Composable
fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    val text = remember { mutableStateOf(value) }
    BasicTextField(
        value = text.value,
        onValueChange = {
            text.value = it
            onValueChange(it)
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier
            .height(30.dp)
            .width(150.dp),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .padding(1.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = OutlinedTextFieldDefaults.shape
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                innerTextField()
            }
        }
    )
}

@Composable
fun BodyText(
    value: String
) {
    Text(
        text = value,
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        modifier = Modifier.padding(horizontal = 8.dp))
}


