package com.jhoel.framepuzzle.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: String,
    val name: String,
    val cover: String?,
    val type: AlbumType,
    @ColumnInfo(name = "created_date") val createdDate: Long,
)
enum class AlbumType { AUTOMATIC, MANUAL }
