package com.jhoel.framepuzzle.di

import com.jhoel.framepuzzle.core.database.FramePuzzleDatabase
import com.jhoel.framepuzzle.core.database.dao.AchievementDao
import com.jhoel.framepuzzle.core.database.dao.AlbumDao
import com.jhoel.framepuzzle.core.database.dao.MemoryDao
import com.jhoel.framepuzzle.core.database.dao.PuzzleDao
import com.jhoel.framepuzzle.core.database.dao.UserDao
import com.jhoel.framepuzzle.core.storage.local.LocalStorageManager
import com.jhoel.framepuzzle.core.storage.SettingsRepository
import org.koin.dsl.module

val appModule = module {
    single { LocalStorageManager(get()) }
    single { SettingsRepository(get()) }

    single {
        androidx.room.Room.databaseBuilder(
            get(),
            FramePuzzleDatabase::class.java,
            "framepuzzle.db"
        ).build()
    }
    single { get<FramePuzzleDatabase>().userDao() }
    single { get<FramePuzzleDatabase>().memoryDao() }
    single { get<FramePuzzleDatabase>().puzzleDao() }
    single { get<FramePuzzleDatabase>().albumDao() }
    single { get<FramePuzzleDatabase>().achievementDao() }
}
