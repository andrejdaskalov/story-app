package com.adaskalov.storyapp.modules

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adaskalov.storyapp.repository.StoryDao
import com.adaskalov.storyapp.repository.model.ActionModel
import com.adaskalov.storyapp.repository.model.ChatMessageModel
import com.adaskalov.storyapp.repository.model.StoryModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Database(entities = [ChatMessageModel::class, StoryModel::class, ActionModel::class], version = 1)
abstract class StoryDatabase : RoomDatabase(){
    abstract fun storyDao(): StoryDao
}

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    fun provideRoomModule(application: Application): StoryDatabase {
        return Room.databaseBuilder(
            application,
            StoryDatabase::class.java,
            "story_database"
        ).build()
    }
}