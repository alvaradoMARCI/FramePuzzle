package com.jhoel.framepuzzle.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "memories", indices = [Index("album_id")])
data class MemoryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val originalImage: String,
    val editedImage: String?,
    val createdDate: Long,
    @ColumnInfo(name = "album_id") val albumId: String?,
    val progress: Float,
    val favorite: Boolean,
)
