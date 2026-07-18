package com.jhoel.framepuzzle.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Álbum.
 *
 * Tipos (sección 22):
 *  - Automáticos (por fecha, eventos, categorías).
 *  - Manuales (familia, amigos, viajes, etc.).
 *
 * Campos según FramePuzzle_Master_Document (sección 34, Entidad Álbum):
 *  - id, name, cover, createdDate
 */
@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "cover") val cover: String?,
    @ColumnInfo(name = "type") val type: AlbumType,
    @ColumnInfo(name = "created_date") val createdDate: Long,
)

enum class AlbumType { AUTOMATIC, MANUAL }
