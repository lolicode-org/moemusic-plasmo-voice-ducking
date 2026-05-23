package org.lolicode.moemusic.pvducking

import org.lolicode.moemusic.api.client.ClientVolumeOverride

internal sealed interface DuckingOverrideUpdate {
    data object NoChange : DuckingOverrideUpdate

    data class Changed(
        val override: ClientVolumeOverride?,
    ) : DuckingOverrideUpdate
}

internal class DuckingRuntimeState(
    initialConfig: DuckingConfig = DuckingConfig(),
) {

    private val activeSources: MutableSet<Any> = linkedSetOf()

    private var config: DuckingConfig = initialConfig.normalized()

    private var appliedOverride: ClientVolumeOverride? = null

    fun currentDesiredOverride(): ClientVolumeOverride? =
        if (config.enabled && activeSources.isNotEmpty()) config.toVolumeOverride() else null

    fun updateConfig(newConfig: DuckingConfig): DuckingOverrideUpdate =
        reconcile(newConfig = newConfig.normalized())

    fun onSourceBecameAudible(source: Any): DuckingOverrideUpdate {
        if (!activeSources.add(source)) return DuckingOverrideUpdate.NoChange
        return reconcile()
    }

    fun onSourceBecameInactive(source: Any): DuckingOverrideUpdate {
        if (!activeSources.remove(source)) return DuckingOverrideUpdate.NoChange
        return reconcile()
    }

    fun clearAllSources(): DuckingOverrideUpdate {
        if (activeSources.isEmpty()) return reconcile()
        activeSources.clear()
        return reconcile()
    }

    private fun reconcile(newConfig: DuckingConfig = config): DuckingOverrideUpdate {
        config = newConfig
        val desiredOverride = currentDesiredOverride()
        if (desiredOverride == appliedOverride) return DuckingOverrideUpdate.NoChange
        appliedOverride = desiredOverride
        return DuckingOverrideUpdate.Changed(desiredOverride)
    }
}
