package com.jhoel.framepuzzle.di

import androidx.room.Room
import com.jhoel.framepuzzle.core.database.FramePuzzleDatabase
import com.jhoel.framepuzzle.core.domain.repository.MemoryRepository
import com.jhoel.framepuzzle.core.domain.repository.MemoryRepositoryImpl
import com.jhoel.framepuzzle.core.domain.repository.UserRepository
import com.jhoel.framepuzzle.core.domain.repository.UserRepositoryImpl
import com.jhoel.framepuzzle.core.domain.usecase.CreateMemoryUseCase
import com.jhoel.framepuzzle.core.storage.SettingsRepository
import com.jhoel.framepuzzle.core.storage.local.LocalStorageManager
import com.jhoel.framepuzzle.feature.camera.CameraViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LocalStorageManager(get()) }
    single { SettingsRepository(get()) }

    single {
        Room.databaseBuilder(
            get(),
            FramePuzzleDatabase::class.java,
            "framepuzzle.db"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<FramePuzzleDatabase>().userDao() }
    single { get<FramePuzzleDatabase>().memoryDao() }
    single { get<FramePuzzleDatabase>().puzzleDao() }
    single { get<FramePuzzleDatabase>().albumDao() }
    single { get<FramePuzzleDatabase>().achievementDao() }

    single<UserRepository> { UserRepositoryImpl(get()) }
    single<MemoryRepository> { MemoryRepositoryImpl(get()) }

    single { CreateMemoryUseCase(get(), get<LocalStorageManager>().originalDir) }

    viewModel { CameraViewModel(get()) }
}
