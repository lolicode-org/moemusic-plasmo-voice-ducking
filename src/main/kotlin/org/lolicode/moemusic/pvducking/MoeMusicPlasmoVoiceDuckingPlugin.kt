package org.lolicode.moemusic.pvducking

import org.lolicode.moemusic.api.LocalizedText
import org.lolicode.moemusic.api.event.OnClientConnected
import org.lolicode.moemusic.api.event.OnClientDisconnected
import org.lolicode.moemusic.api.event.subscribe
import org.lolicode.moemusic.api.plugin.ClientRuntimeContext
import org.lolicode.moemusic.api.plugin.Plugin
import org.lolicode.moemusic.api.plugin.PluginConfigSpec
import org.lolicode.moemusic.api.plugin.pluginConfigSpec
import su.plo.voice.api.addon.ClientAddonsLoader

object MoeMusicPlasmoVoiceDuckingPlugin : Plugin {
    const val MOD_ID = "moemusic_pv_ducking"
    const val VERSION = "1.2.0"

    override val id: String = MOD_ID

    override val displayName: LocalizedText = LocalizedText.key("plugin.moemusic_pv_ducking")

    override val configSpec: PluginConfigSpec<DuckingConfig> = pluginConfigSpec(::DuckingConfig) {
        boolean(
            key = "enabled",
            getter = DuckingConfig::enabled,
            updater = { config, enabled -> config.copy(enabled = enabled) },
        )
        enumSelector(
            key = "volume_mode",
            getter = DuckingConfig::volumeMode,
            updater = { config, mode -> config.copy(volumeMode = mode) },
        )
        intSlider(
            key = "value",
            min = 0,
            max = 100,
            getter = DuckingConfig::value,
            updater = { config, value -> config.copy(value = value.coerceIn(0, 100)) },
        )
    }

    override val version: String = VERSION

    override val supportedApiVersions: String = ">=2.0.0 <3.0.0"

    override fun onClientRuntimeLoad(ctx: ClientRuntimeContext) {
        val initialConfig = ctx.loadConfig(configSpec).normalized()
        PvDuckingAddon.bind(ctx, initialConfig)
        ctx.eventBus.subscribe<OnClientConnected> {
            PvDuckingAddon.onClientConnect()
        }
        ctx.eventBus.subscribe<OnClientDisconnected> {
            PvDuckingAddon.onClientDisconnect()
        }
        ctx.onConfigChanged(configSpec) { updated ->
            PvDuckingAddon.updateConfig(updated.normalized())
        }
    }

    override fun onClientRuntimeUnload() {
        PvDuckingAddon.onClientRuntimeUnload()
        ClientAddonsLoader.unload(PvDuckingAddon)
    }
}
