package com.jhoel.framepuzzle.core.utils.result

/**
 * Resultado de operaciones FramePuzzle.
 * Reemplaza el uso de excepciones en flujos de dominio.
 *
 * Regla: la capa Domain siempre retorna [FramePuzzleResult].
 * La capa Presentation decide cómo mostrar [Failure].
 */
sealed interface FramePuzzleResult<out T> {
    data class Success<T>(val data: T) : FramePuzzleResult<T>
    data class Failure(val error: Failure) : FramePuzzleResult<Nothing>
}

/**
 * Tipos de fallo conocidos. Cada uno mapea a un mensaje UX distinto.
 */
sealed interface Failure {
    data object NotFound : Failure
    data object PermissionDenied : Failure
    data object StorageFull : Failure
    data object InvalidFormat : Failure
    data object Unauthorized : Failure
    data object NetworkUnavailable : Failure
    data class Unknown(val message: String) : Failure
}

/** Conveniencia para envolver un bloque en un Result. */
inline fun <T> framePuzzleRun(block: () -> T): FramePuzzleResult<T> = try {
    FramePuzzleResult.Success(block())
} catch (t: Throwable) {
    FramePuzzleResult.Failure(Failure.Unknown(t.message ?: "Error desconocido"))
}

/** Map sobre Success. */
inline fun <T, R> FramePuzzleResult<T>.map(transform: (T) -> R): FramePuzzleResult<R> =
    when (this) {
        is FramePuzzleResult.Success -> FramePuzzleResult.Success(transform(data))
        is FramePuzzleResult.Failure -> this
    }

/** getOrNull seguro. */
fun <T> FramePuzzleResult<T>.getOrNull(): T? = (this as? FramePuzzleResult.Success)?.data
