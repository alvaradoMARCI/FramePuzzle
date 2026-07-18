package com.jhoel.framepuzzle.feature.library.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UserTest {

    @Test
    fun `create genera usuario nivel 1 sin XP`() {
        val u = User.create("Jhoel")
        assertThat(u.level).isEqualTo(1)
        assertThat(u.xp).isEqualTo(0)
        assertThat(u.name).isEqualTo("Jhoel")
    }

    @Test
    fun `create usa nombre por defecto si vacio`() {
        val u = User.create("  ")
        assertThat(u.name).isEqualTo("Usuario FramePuzzle")
    }

    @Test
    fun `xpForNextLevel escala con el nivel`() {
        val u1 = User.create("a")
        assertThat(u1.xpForNextLevel()).isEqualTo(100)

        val u5 = u1.copy(level = 5)
        assertThat(u5.xpForNextLevel()).isEqualTo(500)
    }

    @Test
    fun `canLevelUp true cuando XP supera umbral`() {
        val u = User.create("a").copy(xp = 150)
        assertThat(u.canLevelUp()).isTrue()
    }

    @Test
    fun `levelUp consume XP correctamente`() {
        val u = User.create("a").copy(xp = 250)
        val leveled = u.levelUp()
        assertThat(leveled.level).isEqualTo(2)
        // 100*1 = 100 consumidos en nivel 1 → quedan 150
        assertThat(leveled.xp).isEqualTo(150)
    }

    @Test
    fun `levelUp multiple respeta curva`() {
        val u = User.create("a").copy(xp = 350)
        val leveled = u.levelUp()
        // nivel 1 consume 100 → quedan 250
        // nivel 2 consume 200 → quedan 50
        assertThat(leveled.level).isEqualTo(3)
        assertThat(leveled.xp).isEqualTo(50)
    }
}
