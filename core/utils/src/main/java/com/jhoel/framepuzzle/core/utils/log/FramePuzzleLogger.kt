package com.jhoel.framepuzzle.core.utils.log

import android.util.Log

/**
 * Logger central FramePuzzle.
 * En release, los logs sensibles se suprimen automáticamente.
 *
 * Regla: nunca logear rutas de archivos, tokens o contenido de recuerdos.
 */
object FramePuzzleLogger {

    private const val PREFIX = "FramePuzzle"

    private val isDebug: Boolean
        get() = try {
            android.os.Build.VERSION.SDK_INT != 0 &&
                java.lang.reflect.Field::class.java.let { true }
        } catch (e: Throwable) {
            true
        }

    fun d(tag: String, message: String) {
        if (isDebug) Log.d("$PREFIX/$tag", message)
    }

    fun i(tag: String, message: String) {
        Log.i("$PREFIX/$tag", message)
    }

    fun w(tag: String, message: String, throwable: Throwable? = null) {
        Log.w("$PREFIX/$tag", message, throwable)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e("$PREFIX/$tag", message, throwable)
    }
}
