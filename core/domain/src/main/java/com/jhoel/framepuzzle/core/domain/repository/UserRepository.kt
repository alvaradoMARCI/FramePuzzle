package com.jhoel.framepuzzle.core.domain.repository

import com.jhoel.framepuzzle.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserRepository {
    fun observeUser(): Flow<User?>
    suspend fun getCurrentUser(): User?
    suspend fun createUser(name: String): User
    suspend fun updateXp(userId: String, delta: Int, newLevel: Int)
}

class UserRepositoryImpl(
    private val userDao: com.jhoel.framepuzzle.core.database.dao.UserDao
) : UserRepository {

    override fun observeUser(): Flow<User?> =
        userDao.observeCurrentUser().map { it?.toDomain() }

    override suspend fun getCurrentUser(): User? =
        userDao.getCurrentUser()?.toDomain()

    override suspend fun createUser(name: String): User {
        val user = User(
            id = java.util.UUID.randomUUID().toString(),
            name = name.trim().ifBlank { "Usuario FramePuzzle" },
            avatarPath = null,
            level = 1,
            xp = 0,
            createdDate = System.currentTimeMillis()
        )
        userDao.upsert(user.toEntity())
        return user
    }

    override suspend fun updateXp(userId: String, delta: Int, newLevel: Int) {
        userDao.addXp(userId, delta, newLevel)
    }

    private fun com.jhoel.framepuzzle.core.database.entity.UserEntity.toDomain() = User(
        id = id, name = name, avatarPath = avatar,
        level = level, xp = xp, createdDate = createdDate
    )

    private fun User.toEntity() = com.jhoel.framepuzzle.core.database.entity.UserEntity(
        id = id, name = name, avatar = avatarPath,
        level = level, xp = xp, createdDate = createdDate
    )
}
