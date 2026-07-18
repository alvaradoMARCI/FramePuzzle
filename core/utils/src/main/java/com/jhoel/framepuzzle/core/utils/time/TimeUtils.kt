package com.jhoel.framepuzzle.core.utils.time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utilidades de tiempo. FramePuzzle usa ISO-8601 en almacenamiento
 * y formato legible en UI.
 */
object TimeUtils {

    private val isoFormatter by lazy {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US)
    }

    /** Convierte epoch millis a ISO-8601. */
    fun toIso(millis: Long): String = isoFormatter.format(Date(millis))

    /** Convierte ISO-8601 a epoch millis. */
    fun fromIso(iso: String): Long = isoFormatter.parse(iso)?.time ?: 0L

    /** Formato corto legible: "18/07/2026". */
    fun formatShort(millis: Long): String {
        val f = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return f.format(Date(millis))
    }

    /** Formato largo legible: "18 de julio de 2026". */
    fun formatLong(millis: Long): String {
        val f = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale.getDefault())
        return f.format(Date(millis))
    }

    /** Formato tiempo del puzzle: "mm:ss". */
    fun formatPuzzleDuration(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }
}
