package com.jhoel.framepuzzle.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad Recuerdo (Memory).
 *
 * Regla de FramePuzzle (sección 9):
 *  "El recuerdo original nunca debe modificarse".
 *
 * Por eso hay dos columnas: originalImage y editedImage.
 * El flujo es: Original → Ediciones → Puzzle → Experiencia final.
 *
 * Campos según FramePuzzle_Master_Document (sección 34, Entidad Recuerdo):
 *  - id, title, originalImage, editedImage, createdDate, albumId, progress, favorite
 */
@Entity(
    tableName = "memories",
    foreignKeys = [
        ForeignKey(
            entity = AlbumEntity::class,
            parentColumns = ["id"],
            childColumns = ["album_id"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("album_id")],
)
data class MemoryEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "original_image") val originalImage: String,
    @ColumnInfo(name = "edited_image") val editedImage: String?,
    @ColumnInfo(name = "created_date") val createdDate: Long,
    @ColumnInfo(name = "album_id") val albumId: String?,
    @ColumnInfo(name = "progress") val progress: Float,
    @ColumnInfo(name = "favorite") val favorite: Boolean,
)
