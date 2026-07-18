package com.jhoel.framepuzzle.core.database.di

import android.content.Context
import androidx.room.Room
import com.jhoel.framepuzzle.core.database.FramePuzzleDatabase
import com.jhoel.framepuzzle.core.database.dao.AchievementDao
import com.jhoel.framepuzzle.core.database.dao.AlbumDao
import com.jhoel.framepuzzle.core.database.dao.MemoryDao
import com.jhoel.framepuzzle.core.database.dao.PuzzleDao
import com.jhoel.framepuzzle.core.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt para core:database.
 *
 * La DB se crea una sola vez como Singleton.
 * El nombre del archivo DB se mantiene privado (no se loguea ni expone).
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFramePuzzleDatabase(
        @ApplicationContext context: Context,
    ): FramePuzzleDatabase = Room.databaseBuilder(
        context,
        FramePuzzleDatabase::class.java,
        FramePuzzleDatabase.NAME,
    )
        .fallbackToDestructiveMigration(onDowngrade = true)
        .build()

    @Provides
    fun provideUserDao(db: FramePuzzleDatabase): UserDao = db.userDao()

    @Provides
    fun provideMemoryDao(db: FramePuzzleDatabase): MemoryDao = db.memoryDao()

    @Provides
    fun providePuzzleDao(db: FramePuzzleDatabase): PuzzleDao = db.puzzleDao()

    @Provides
    fun provideAlbumDao(db: FramePuzzleDatabase): AlbumDao = db.albumDao()

    @Provides
    fun provideAchievementDao(db: FramePuzzleDatabase): AchievementDao = db.achievementDao()
}
