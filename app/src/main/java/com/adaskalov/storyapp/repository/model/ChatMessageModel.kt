package com.adaskalov.storyapp.repository.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = StoryModel::class,
        parentColumns = ["id"],
        childColumns = ["storyId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ChatMessageModel (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val message: String,
    val sender: String,
    val storyId: Long

    )