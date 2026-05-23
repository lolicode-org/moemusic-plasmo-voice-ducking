package org.lolicode.moemusic.pvducking

import kotlinx.serialization.Serializable
import org.lolicode.moemusic.api.client.ClientVolumeOverride

@Serializable
enum class DuckingVolumeMode {
    FIXED_PERCENT,
    PERCENT_OF_CURRENT,
}

@Serializable
data class DuckingConfig(
    val enabled: Boolean = true,
    val volumeMode: DuckingVolumeMode = DuckingVolumeMode.PERCENT_OF_CURRENT,
    val value: Int = 25,
) {
    fun normalized(): DuckingConfig = copy(value = value.coerceIn(0, 100))

    fun toVolumeOverride(): ClientVolumeOverride = when (volumeMode) {
        DuckingVolumeMode.FIXED_PERCENT ->
            ClientVolumeOverride.FixedPercent(value)

        DuckingVolumeMode.PERCENT_OF_CURRENT ->
            ClientVolumeOverride.PercentOfConfiguredVolume(value)
    }
}
