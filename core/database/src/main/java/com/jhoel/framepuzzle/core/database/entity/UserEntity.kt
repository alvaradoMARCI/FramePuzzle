package com.jhoel.framepuzzle.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Usuario.
 *
 * FramePuzzle es una app local-first: el usuario vive en el dispositivo.
 * No hay backend ni cuentas remotas.
 *
 * Campos según FramePuzzle_Master_Document (sección 34, Entidad Usuario):
 *  - id
 *  - name
 *  - avatar
 *  - level
 *  - xp
 *  - createdDate
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "avatar") val avatar: String?,
    @ColumnInfo(name = "level") val level: Int,
    @ColumnInfo(name = "xp") val xp: Int,
    @ColumnInfo(name = "created_date") val createdDate: Long,
)
