package com.adaskalov.storyapp

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.adaskalov.storyapp.ui.screens.HistoryScreen
import com.adaskalov.storyapp.ui.screens.MainScreen
import com.adaskalov.storyapp.ui.screens.NewChatScreen
import com.adaskalov.storyapp.ui.theme.StoryAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import java.util.Locale


@AndroidEntryPoint
class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private val TAG = "MainActivitySpeech"
    private val mTts: TextToSpeech by lazy { TextToSpeech(this, this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            StoryAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = NewChatScreenDestination,
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { -it / 3 },
                            animationSpec = tween(300)
                        )
                    },
                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -it / 3 },
                            animationSpec = tween(300)
                        )
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                ) {
                    composable<MainScreenDestination> {
                        val destination = it.toRoute<MainScreenDestination>()
                        MainScreen(
                            topic = destination.topic,
                            setting = destination.setting,
                            style = destination.style,
                            goBack = { navController.popBackStack() },
                            speak = { text -> speak(text) },
                            stopSpeaking = { mTts.stop() },
                            isSpeaking = { mTts.isSpeaking },
                        )
                    }

                    composable<NewChatScreenDestination> {
                        NewChatScreen(
                            navigateToMainScreen = { topic, setting, style ->
                                navController.navigate(MainScreenDestination(topic, setting, style))
                            },
                            navigateToHistoryScreen = {
                                navController.navigate(HistoryScreenDestination)
                            }
                        )
                    }

                    composable<HistoryScreenDestination> {
                        HistoryScreen(
                            goBack = { navController.popBackStack() },
                            navigateToStory = {story ->
                                story.id?.let { id ->
                                    navController.navigate(MainScreenExistingDestination(id))
                                }
                            }
                        )
                    }

                    composable<MainScreenExistingDestination> {
                        val destination = it.toRoute<MainScreenExistingDestination>()
                        MainScreen(
                            storyId = destination.storyId,
                            goBack = { navController.popBackStack() },
                            speak = { text -> speak(text) },
                            stopSpeaking = { mTts.stop() },
                            isSpeaking = { mTts.isSpeaking },
                        )
                    }
                }
            }
        }
    }

    override fun onInit(status: Int) {
        Toast.makeText(this, "TTS initializing", Toast.LENGTH_SHORT).show()
        if (status == TextToSpeech.SUCCESS) {
            val result: Int = mTts.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                Log.e(TAG, "Language not supported")
            } else {
                Log.i(TAG, "Initialization successful")
            }
        } else {
            Log.e(TAG, "Initialization failed")
        }
    }

    override fun onDestroy() {
        if (mTts.isSpeaking) {
            mTts.stop()
        }
        mTts.shutdown()
        super.onDestroy()
    }

    private fun speak(text: String) {
        mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}

@Serializable
data class MainScreenDestination(val topic: String, val setting: String, val style: String)

@Serializable
data class MainScreenExistingDestination(val storyId: Long)

@Serializable
data object NewChatScreenDestination
@Serializable
data object HistoryScreenDestination
