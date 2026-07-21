package com.jhoel.framepuzzle.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jhoel.framepuzzle.core.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users LIMIT 1")
    fun observeCurrentUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

    @Query("UPDATE users SET xp = xp + :delta, level = :newLevel WHERE id = :userId")
    suspend fun addXp(userId: String, delta: Int, newLevel: Int)

    @Query("UPDATE users SET avatar = :avatarPath WHERE id = :userId")
    suspend fun updateAvatar(userId: String, avatarPath: String?)
}
