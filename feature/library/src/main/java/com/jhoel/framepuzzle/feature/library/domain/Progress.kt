package com.jhoel.framepuzzle.feature.library.domain

import com.jhoel.framepuzzle.core.utils.result.Failure
import com.jhoel.framepuzzle.core.utils.result.FramePuzzleResult

/**
 * Sistema de progreso (sección 19).
 *
 * Eventos que otorgan XP:
 *  - Completar recuerdo: +XP
 *  - Resolver dificultad alta: +XP
 *  - Crear álbum: +XP
 *  - Mantener actividad: +XP
 *
 * Esta enum codifica los eventos conocidos y su recompensa.
 */
enum class XpEvent(val xpReward: Int, val display: String) {
    MEMORY_CREATED(20, "Recuerdo creado"),
    MEMORY_EDITED(10, "Recuerdo editado"),
    PUZZLE_SOLVED_EASY(15, "Puzzle fácil resuelto"),
    PUZZLE_SOLVED_NORMAL(30, "Puzzle normal resuelto"),
    PUZZLE_SOLVED_HARD(60, "Puzzle difícil resuelto"),
    PUZZLE_SOLVED_PERFECT(50, "Puzzle perfecto (sin movimientos extra)"),
    ALBUM_CREATED(25, "Álbum creado"),
    FIRST_MEMORY(50, "Primer recuerdo"),
    DAILY_ACTIVITY(5, "Actividad diaria"),
}

/**
 * Caso de uso: añadir XP al usuario y manejar subida de nivel.
 */
class AddXpUseCase {

    operator fun invoke(user: User, event: XpEvent): User {
        val updated = user.copy(xp = user.xp + event.xpReward)
        return updated.levelUp()
    }
}

/**
 * Caso de uso: crear usuario local por primera vez.
 */
class CreateUserUseCase {

    operator fun invoke(name: String, avatarPath: String? = null): FramePuzzleResult<User> =
        try {
            FramePuzzleResult.Success(User.create(name, avatarPath))
        } catch (t: Throwable) {
            FramePuzzleResult.Failed(Failure.Unknown(t.message ?: "Error creando usuario"))
        }
}
