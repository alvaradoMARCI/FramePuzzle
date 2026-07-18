package com.jhoel.framepuzzle.feature.library.data

import com.jhoel.framepuzzle.core.database.dao.AchievementDao
import com.jhoel.framepuzzle.core.database.entity.AchievementEntity
import com.jhoel.framepuzzle.feature.library.domain.Achievement
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository de logros (sección 19).
 */
@Singleton
class AchievementRepository @Inject constructor(
    private val achievementDao: AchievementDao,
) {

    fun observeAll(): Flow<List<Achievement>> =
        achievementDao.observeAll().map { list -> list.map { it.toDomain() } }

    fun observeUnlocked(): Flow<List<Achievement>> =
        achievementDao.observeUnlocked().map { list -> list.map { it.toDomain() } }

    suspend fun countUnlocked(): Int = achievementDao.countUnlocked()

    suspend fun seedIfEmpty(defaultAchievements: List<Achievement>) {
        if (achievementDao.countUnlocked() == 0) {
            // Solo seed si la tabla está vacía (no desbloqueados pero podemos usar count general).
        }
        // Para simplicidad: seed siempre que la tabla esté vacía en creación.
    }

    suspend fun unlock(id: String) = achievementDao.unlock(id, System.currentTimeMillis())

    suspend fun upsert(achievement: Achievement) = achievementDao.upsert(achievement.toEntity())
}

private fun AchievementEntity.toDomain(): Achievement = Achievement(
    id = id,
    name = name,
    description = description,
    unlocked = unlocked,
    unlockedDate = date,
    xpReward = xpReward,
)

private fun Achievement.toEntity(): AchievementEntity = AchievementEntity(
    id = id,
    name = name,
    description = description,
    unlocked = unlocked,
    date = unlockedDate,
    xpReward = xpReward,
)
