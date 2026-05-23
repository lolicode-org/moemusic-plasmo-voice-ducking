package org.lolicode.moemusic.pvducking

import org.lolicode.moemusic.api.client.ClientVolumeOverride
import kotlin.test.Test
import kotlin.test.assertEquals

class DuckingRuntimeStateTest {

    @Test
    fun `enabled ducking applies override when first source becomes audible`() {
        val state = DuckingRuntimeState(DuckingConfig(enabled = true, volumeMode = DuckingVolumeMode.FIXED_PERCENT, value = 35))

        val result = state.onSourceBecameAudible("speaker-a")

        assertEquals(
            DuckingOverrideUpdate.Changed(ClientVolumeOverride.FixedPercent(35)),
            result,
        )
    }

    @Test
    fun `additional active sources do not re-emit unchanged override`() {
        val state = DuckingRuntimeState(DuckingConfig(value = 40))

        state.onSourceBecameAudible("speaker-a")
        val second = state.onSourceBecameAudible("speaker-b")

        assertEquals(DuckingOverrideUpdate.NoChange, second)
    }

    @Test
    fun `override clears when last active source disappears`() {
        val state = DuckingRuntimeState(DuckingConfig(value = 40))

        state.onSourceBecameAudible("speaker-a")
        assertEquals(
            DuckingOverrideUpdate.Changed(null),
            state.onSourceBecameInactive("speaker-a"),
        )
    }

    @Test
    fun `config change while active re-emits new override`() {
        val state = DuckingRuntimeState(DuckingConfig(value = 40))

        state.onSourceBecameAudible("speaker-a")
        val updated = state.updateConfig(
            DuckingConfig(
                enabled = true,
                volumeMode = DuckingVolumeMode.FIXED_PERCENT,
                value = 25,
            )
        )

        assertEquals(
            DuckingOverrideUpdate.Changed(ClientVolumeOverride.FixedPercent(25)),
            updated,
        )
    }

    @Test
    fun `disabling config while active clears override`() {
        val state = DuckingRuntimeState(DuckingConfig(value = 40))

        state.onSourceBecameAudible("speaker-a")
        assertEquals(
            DuckingOverrideUpdate.Changed(null),
            state.updateConfig(DuckingConfig(enabled = false, value = 40)),
        )
    }
}
