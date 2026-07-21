package com.jhoel.framepuzzle

import android.app.Application
import com.jhoel.framepuzzle.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FramePuzzleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FramePuzzleApp)
            modules(appModule)
        }
    }
}
