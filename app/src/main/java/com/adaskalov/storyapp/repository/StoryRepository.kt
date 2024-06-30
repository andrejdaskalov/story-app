package com.adaskalov.storyapp.repository

import com.adaskalov.storyapp.domain.ChatMessage
import com.adaskalov.storyapp.domain.Story
import com.adaskalov.storyapp.domain.toMessageAuthor
import com.adaskalov.storyapp.modules.StoryDatabase
import com.adaskalov.storyapp.repository.model.ChatMessageModel
import com.adaskalov.storyapp.repository.model.StoryModel
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyDatabase: StoryDatabase
) {
    private val storyDao = storyDatabase.storyDao()

    fun getStories(): List<Story> {
        return storyDao.getAllStories().map {
            Story(id = it.id, title =  it.title, messages = emptyList())
        }
    }

    fun getStoryById(id: Long): Story {
        val story = storyDao.getStoryById(id)
        val messages = storyDao.getChatMessagesByStoryId(id).map {
            ChatMessage(it.message, it.sender.toMessageAuthor())
        }
        return Story(id= story.id, title = story.title, messages = messages)
    }

    fun insertMessage(chatMessage: ChatMessage, storyId: Long) {
        storyDao.insertMessage(
            ChatMessageModel(
                0,
                chatMessage.message,
                chatMessage.messageBy.name,
                storyId
            )
        )
    }

    fun insertStory(story: Story) : Long {
        val id = storyDao.insertStory(
            StoryModel(
                0,
                story.title
            )
        )
        story.messages.forEach {
            insertMessage(it, id)
        }
        return id
    }
}