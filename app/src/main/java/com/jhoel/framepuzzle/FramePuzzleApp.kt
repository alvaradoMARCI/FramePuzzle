package com.jhoel.framepuzzle

import android.app.Application
import android.util.Log
import com.jhoel.framepuzzle.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.error.KoinAppAlreadyStartedException

class FramePuzzleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            startKoin {
                androidContext(this@FramePuzzleApp)
                modules(appModule)
            }
        } catch (e: Throwable) {
            Log.e("FramePuzzleApp", "FATAL: Koin initialization failed", e)
            // No crashear - la app intentará funcionar sin DI
        }
    }
}
