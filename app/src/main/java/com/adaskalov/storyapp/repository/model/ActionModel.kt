package com.adaskalov.storyapp.repository.model

import androidx.room.ColumnInfo
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
data class ActionModel(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val action: String,
    val storyId: Long
)
