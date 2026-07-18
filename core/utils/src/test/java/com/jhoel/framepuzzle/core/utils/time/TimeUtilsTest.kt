package com.jhoel.framepuzzle.core.utils.time

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TimeUtilsTest {

    @Test
    fun `toIso y fromIso son inversas`() {
        val original = 1721304000000L
        val iso = TimeUtils.toIso(original)
        val back = TimeUtils.fromIso(iso)
        assertThat(back).isEqualTo(original)
    }

    @Test
    fun `formatPuzzleDuration mm_ss`() {
        val duration = (3 * 60 + 25) * 1000L
        assertThat(TimeUtils.formatPuzzleDuration(duration)).isEqualTo("03:25")
    }

    @Test
    fun `formatPuzzleDuration zero`() {
        assertThat(TimeUtils.formatPuzzleDuration(0L)).isEqualTo("00:00")
    }
}
