package com.jhoel.framepuzzle.core.utils.result

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FramePuzzleResultTest {

    @Test
    fun `Success map transforma el valor`() {
        val result: FramePuzzleResult<Int> = FramePuzzleResult.Success(10)
        val mapped = result.map { it * 2 }
        assertThat(mapped).isEqualTo(FramePuzzleResult.Success(20))
    }

    @Test
    fun `Failure map propaga el error`() {
        val result: FramePuzzleResult<Int> = FramePuzzleResult.Failure(Failure.NotFound)
        val mapped = result.map { it * 2 }
        assertThat(mapped).isEqualTo(FramePuzzleResult.Failure(Failure.NotFound))
    }

    @Test
    fun `framePuzzleRun envuelve exito`() {
        val result = framePuzzleRun { 42 }
        assertThat(result).isEqualTo(FramePuzzleResult.Success(42))
    }

    @Test
    fun `framePuzzleRun envuelve excepcion`() {
        val result = framePuzzleRun<Int> { error("boom") }
        assertThat(result).isInstanceOf(FramePuzzleResult.Failure::class.java)
    }

    @Test
    fun `getOrNull devuelve valor en Success`() {
        assertThat(FramePuzzleResult.Success("x").getOrNull()).isEqualTo("x")
        assertThat(FramePuzzleResult.Failure(Failure.Unknown("x")).getOrNull()).isNull()
    }
}
