package com.jhoel.framepuzzle.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Logro (Achievement).
 *
 * Sistema de progreso (sección 19):
 *  - XP, niveles, logros y evolución del avatar.
 *
 * Campos según FramePuzzle_Master_Document (sección 34, Entidad Logro):
 *  - id, name, description, unlocked, date
 */
@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "unlocked") val unlocked: Boolean,
    @ColumnInfo(name = "unlocked_date") val date: Long?,
    @ColumnInfo(name = "xp_reward") val xpReward: Int,
)
