package com.jhoel.framepuzzle.feature.puzzle.data

import com.jhoel.framepuzzle.core.database.dao.PuzzleDao
import com.jhoel.framepuzzle.core.database.entity.PuzzleDifficulty
import com.jhoel.framepuzzle.core.database.entity.PuzzleEntity
import com.jhoel.framepuzzle.core.database.entity.PuzzleType
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleStats
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository de puzzles (sección 31, ejemplo PuzzleRepository).
 */
@Singleton
class PuzzleRepository @Inject constructor(
    private val puzzleDao: PuzzleDao,
) {

    fun observeByMemory(memoryId: String): Flow<List<PuzzleEntity>> =
        puzzleDao.observeByMemory(memoryId)

    fun observeCompleted(): Flow<List<PuzzleEntity>> =
        puzzleDao.observeCompleted()

    suspend fun getById(id: String): PuzzleEntity? = puzzleDao.getById(id)

    suspend fun create(entity: PuzzleEntity) = puzzleDao.upsert(entity)

    suspend fun markCompleted(id: String, stats: PuzzleStats) =
        puzzleDao.markCompleted(id, true, stats.timeMillis, stats.moves)

    suspend fun countCompleted(): Int = puzzleDao.countCompleted()

    suspend fun deleteByMemory(memoryId: String) = puzzleDao.deleteByMemory(memoryId)
}
