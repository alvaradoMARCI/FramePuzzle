package com.jhoel.framepuzzle.feature.profile.domain

/**
 * Usuario local de FramePuzzle (sección 34, Entidad Usuario).
 *
 * FramePuzzle es local-first: el usuario existe únicamente en el dispositivo.
 */
data class User(
    val id: String,
    val name: String,
    val avatarPath: String?,
    val level: Int,
    val xp: Int,
    val createdDate: Long,
) {
    /** XP necesaria para subir de nivel. Curva ascendente. */
    fun xpForNextLevel(): Int = BASE_XP * level

    /** ¿Está listo para subir de nivel? */
    fun canLevelUp(): Boolean = xp >= xpForNextLevel()

    /** Sube de nivel consumiendo la XP necesaria. */
    fun levelUp(): User {
        var newLevel = level
        var newXp = xp
        while (newXp >= BASE_XP * newLevel) {
            newXp -= BASE_XP * newLevel
            newLevel++
        }
        return copy(level = newLevel, xp = newXp)
    }

    companion object {
        const val BASE_XP = 100

        /**
         * Crea un usuario nuevo (sección 20: avatar inicial, sin XP).
         */
        fun create(name: String, avatarPath: String? = null): User = User(
            id = java.util.UUID.randomUUID().toString(),
            name = name.trim().ifBlank { "Usuario FramePuzzle" },
            avatarPath = avatarPath,
            level = 1,
            xp = 0,
            createdDate = System.currentTimeMillis(),
        )
    }
}

/**
 * Avatar del usuario (sección 20).
 *
 * El avatar evoluciona con el progreso: marcos especiales, dorado, brillos,
 * firmas, insignias y efectos visuales.
 */
data class Avatar(
    val user: User,
    val unlockedFrames: List<AvatarFrame> = listOf(AvatarFrame.NONE),
    val unlockedBadges: List<AvatarBadge> = emptyList(),
    val activeFrame: AvatarFrame = AvatarFrame.NONE,
)

enum class AvatarFrame(val display: String, val requiredLevel: Int) {
    NONE("Sin marco", 1),
    GOLD("Marco dorado", 3),
    EMERALD("Marco esmeralda", 6),
    AMETHYST("Marco amatista", 9),
    LEGEND("Marco legendario", 12),
}

enum class AvatarBadge(val display: String) {
    FIRST_MEMORY("Primer recuerdo"),
    TEN_MEMORIES("10 recuerdos"),
    FIRST_PUZZLE("Primer puzzle resuelto"),
    PERFECT_PUZZLE("Puzzle perfecto"),
    BACKUP_MASTER("Maestro del respaldo"),
}
