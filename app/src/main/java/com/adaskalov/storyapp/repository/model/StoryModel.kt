package com.adaskalov.storyapp.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class StoryModel (
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "story_title") val title: String,

)