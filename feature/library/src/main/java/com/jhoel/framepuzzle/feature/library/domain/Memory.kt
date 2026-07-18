package com.jhoel.framepuzzle.feature.library.domain

/**
 * Recuerdo FramePuzzle (sección 9: Sistema de recuerdos).
 *
 * Regla principal: el recuerdo original nunca debe modificarse.
 * La app conserva: Original → Ediciones → Puzzle → Experiencia final.
 */
data class Memory(
    val id: String,
    val title: String,
    val originalImagePath: String,
    val editedImagePath: String?,
    val createdDate: Long,
    val albumId: String?,
    val progress: Float,
    val favorite: Boolean,
)

/**
 * Álbum (sección 22).
 *
 * Tipos:
 *  - Automáticos: por fecha, eventos, categorías.
 *  - Manuales: familia, amigos, viajes, etc.
 */
data class Album(
    val id: String,
    val name: String,
    val coverPath: String?,
    val isAutomatic: Boolean,
    val createdDate: Long,
)

/**
 * Logro (sección 19: Sistema de progreso).
 */
data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val unlocked: Boolean,
    val unlockedDate: Long?,
    val xpReward: Int,
)
