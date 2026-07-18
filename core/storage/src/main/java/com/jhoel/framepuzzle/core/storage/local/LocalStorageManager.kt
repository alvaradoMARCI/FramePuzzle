package com.jhoel.framepuzzle.core.storage.local

import android.content.Context
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Administrador del almacenamiento interno FramePuzzle (sección 35).
 *
 * Principios:
 *  - El teléfono es el almacenamiento principal.
 *  - No nube obligatoria.
 *  - No carpetas visibles innecesarias en almacenamiento público.
 *
 * Estructura:
 *   FramePuzzle/
 *     ├── memories/
 *     ├── original/
 *     ├── edited/
 *     ├── puzzles/
 *     └── backup/
 *
 * Todos los archivos viven en el almacenamiento interno de la app
 * (no accesible desde otras apps sin permisos explícitos vía FileProvider).
 */
@Singleton
class LocalStorageManager @javax.inject.Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext appContext: Context,
) {
    private val context: Context = appContext.applicationContext

    /** Directorio raíz interno: /data/data/<pkg>/files/FramePuzzle/ */
    val rootDir: File
        get() {
            val dir = File(context.filesDir, ROOT_DIR_NAME)
            if (!dir.exists()) dir.mkdirs()
            return dir
        }

    val memoriesDir: File
        get() = ensureSubdir("memories")

    val originalDir: File
        get() = ensureSubdir("original")

    val editedDir: File
        get() = ensureSubdir("edited")

    val puzzlesDir: File
        get() = ensureSubdir("puzzles")

    val backupDir: File
        get() = ensureSubdir("backup")

    val exportsDir: File
        get() {
            val dir = File(context.cacheDir, "exports")
            if (!dir.exists()) dir.mkdirs()
            return dir
        }

    /** Ruta relativa al root interno, para guardar en DB. */
    fun relativePath(file: File): String =
        file.absolutePath.removePrefix(context.filesDir.absolutePath).removePrefix(File.separator)

    /** Reconstruye un File desde la ruta relativa almacenada. */
    fun resolveRelative(relative: String): File = File(context.filesDir, relative)

    /** Crea archivo destino para una imagen original. */
    fun newOriginalFile(memoryId: String): File =
        File(originalDir, "$memoryId.jpg")

    /** Crea archivo destino para una imagen editada. */
    fun newEditedFile(memoryId: String): File =
        File(editedDir, "${memoryId}_edited.jpg")

    /** Crea archivo destino para la imagen final del puzzle resuelto. */
    fun newPuzzleFile(puzzleId: String): File =
        File(puzzlesDir, "$puzzleId.png")

    /** Crea archivo destino para un respaldo. */
    fun newBackupFile(suffix: String = ""): File =
        File(backupDir, "framepuzzle${if (suffix.isBlank()) "" else "_$suffix"}.fpbackup")

    /** Espacio ocupado por FramePuzzle en disco (bytes). */
    fun usedSpaceBytes(): Long {
        val files = mutableListOf<File>()
        collectFiles(rootDir, files)
        return files.sumOf { it.length() }
    }

    /** Limpia archivos temporales de exports. */
    fun clearExports() {
        exportsDir.listFiles()?.forEach { it.delete() }
    }

    private fun ensureSubdir(name: String): File {
        val dir = File(rootDir, name)
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private fun collectFiles(dir: File, acc: MutableList<File>) {
        val children = dir.listFiles() ?: return
        for (c in children) {
            if (c.isDirectory) collectFiles(c, acc) else acc.add(c)
        }
    }

    companion object {
        const val ROOT_DIR_NAME = "FramePuzzle"
    }
}
