package com.adaskalov.storyapp.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.adaskalov.storyapp.repository.model.ChatMessageModel
import com.adaskalov.storyapp.repository.model.StoryModel

@Dao
interface StoryDao {

    @Query("SELECT * FROM StoryModel")
    fun getAllStories() : List<StoryModel>

    @Query("SELECT * FROM StoryModel WHERE id = :id")
    fun getStoryById(id: Long) : StoryModel

    @Query("SELECT * FROM ChatMessageModel WHERE storyId = :storyId")
    fun getChatMessagesByStoryId(storyId: Long) : List<ChatMessageModel>

    @Insert
    fun insertMessage(chatMessageModel: ChatMessageModel) : Long

    @Delete
    fun deleteStory(storyModel: StoryModel)

    @Insert
    fun insertStory(storyModel: StoryModel) : Long
}