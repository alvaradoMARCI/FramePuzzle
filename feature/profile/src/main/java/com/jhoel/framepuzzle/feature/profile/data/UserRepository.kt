package com.jhoel.framepuzzle.feature.profile.data

import com.jhoel.framepuzzle.core.database.dao.UserDao
import com.jhoel.framepuzzle.core.database.entity.UserEntity
import com.jhoel.framepuzzle.feature.profile.domain.User
import com.jhoel.framepuzzle.feature.profile.domain.XpEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository de usuario (sección 31, ejemplo UserRepository).
 */
@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
) {

    fun observeUser(): Flow<User?> =
        userDao.observeCurrentUser().map { it?.toDomain() }

    suspend fun getCurrent(): User? = userDao.getCurrentUser()?.toDomain()

    suspend fun create(user: User) {
        userDao.upsert(user.toEntity())
    }

    suspend fun addXp(event: XpEvent, user: User): User {
        val newXp = user.xp + event.xpReward
        var newLevel = user.level
        var remaining = newXp
        while (remaining >= 100 * newLevel) {
            remaining -= 100 * newLevel
            newLevel++
        }
        val updated = user.copy(level = newLevel, xp = remaining)
        userDao.upsert(updated.toEntity())
        return updated
    }

    suspend fun updateAvatar(avatarPath: String?) {
        val current = userDao.getCurrentUser() ?: return
        userDao.updateAvatar(current.id, avatarPath)
    }
}

private fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    avatarPath = avatar,
    level = level,
    xp = xp,
    createdDate = createdDate,
)

private fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    avatar = avatarPath,
    level = level,
    xp = xp,
    createdDate = createdDate,
)
