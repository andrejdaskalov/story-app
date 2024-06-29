package com.adaskalov.storyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
                            goBack = { navController.popBackStack() }
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
