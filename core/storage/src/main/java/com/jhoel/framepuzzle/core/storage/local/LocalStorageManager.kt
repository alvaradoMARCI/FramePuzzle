package com.jhoel.framepuzzle.core.storage.local

import android.content.Context
import java.io.File

class LocalStorageManager(private val context: Context) {
    val rootDir: File
        get() = File(context.filesDir, "FramePuzzle").also { it.mkdirs() }
    val originalDir: File
        get() = File(rootDir, "original").also { it.mkdirs() }
    val editedDir: File
        get() = File(rootDir, "edited").also { it.mkdirs() }
    val puzzlesDir: File
        get() = File(rootDir, "puzzles").also { it.mkdirs() }
    val backupDir: File
        get() = File(rootDir, "backup").also { it.mkdirs() }

    fun newOriginalFile(id: String) = File(originalDir, "$id.jpg")
    fun newEditedFile(id: String) = File(editedDir, "${id}_edited.jpg")
    fun newPuzzleFile(id: String) = File(puzzlesDir, "$id.png")
}
