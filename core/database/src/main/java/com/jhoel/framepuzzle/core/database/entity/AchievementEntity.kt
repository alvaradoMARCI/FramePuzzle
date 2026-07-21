package com.jhoel.framepuzzle.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val unlocked: Boolean,
    @ColumnInfo(name = "unlocked_date") val unlockedDate: Long?,
    @ColumnInfo(name = "xp_reward") val xpReward: Int,
)
