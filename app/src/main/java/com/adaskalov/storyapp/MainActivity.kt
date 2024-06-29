package com.adaskalov.storyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.adaskalov.storyapp.ui.screens.MainScreen
import com.adaskalov.storyapp.ui.screens.NewChatScreen
import com.adaskalov.storyapp.ui.theme.StoryAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            StoryAppTheme {
                NavHost(navController = navController, startDestination = NewChatScreenDestination) {
                    composable<MainScreenDestination> {
                        val destination = it.toRoute<MainScreenDestination>()
                        MainScreen(
                            topic = destination.topic,
                            setting = destination.setting,
                            style = destination.style
                        )
                    }

                    composable<NewChatScreenDestination> {
                        NewChatScreen(
                            navigateToMainScreen = { topic, setting, style ->
                                navController.navigate(MainScreenDestination(topic, setting, style))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Serializable
data class MainScreenDestination(val topic: String, val setting: String, val style: String)

@Serializable
data object NewChatScreenDestination
