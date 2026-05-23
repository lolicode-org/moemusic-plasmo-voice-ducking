package org.lolicode.moemusic.pvducking

import org.lolicode.moemusic.api.client.ClientVolumeOverride
import org.lolicode.moemusic.api.plugin.ClientRuntimeContext
import su.plo.voice.api.addon.AddonInitializer
import su.plo.voice.api.addon.AddonLoaderScope
import su.plo.voice.api.addon.InjectPlasmoVoice
import su.plo.voice.api.addon.annotation.Addon
import su.plo.voice.api.client.PlasmoVoiceClient
import su.plo.voice.api.client.event.audio.source.AudioSourceResetEvent
import su.plo.voice.api.client.event.audio.source.AudioSourceWriteEvent
import su.plo.voice.api.event.EventSubscribe

@Addon(
    id = "pv-addon-moemusic-ducking",
    name = "MoeMusic Plasmo Voice Ducking",
    version = MoeMusicPlasmoVoiceDuckingPlugin.VERSION,
    authors = ["KoishiMoe", "GPT-5.4"],
    scope = AddonLoaderScope.CLIENT,
)
object PvDuckingAddon : AddonInitializer {

    private const val OWNER_ID: String = MoeMusicPlasmoVoiceDuckingPlugin.MOD_ID

    @InjectPlasmoVoice
    private lateinit var voiceClient: PlasmoVoiceClient

    private val stateLock = Any()
    private val runtimeState = DuckingRuntimeState()

    @Volatile
    private var clientContext: ClientRuntimeContext? = null

    override fun onAddonInitialize() = Unit

    override fun onAddonShutdown() {
        synchronized(stateLock) {
            applyUpdateLocked(
                runtimeState.clearAllSources(),
                forceClear = true,
                clearContextAfter = true,
            )
        }
    }

    fun bind(ctx: ClientRuntimeContext, config: DuckingConfig) {
        synchronized(stateLock) {
            clientContext = ctx
            runtimeState.updateConfig(config)
            applyDesiredOverrideLocked(
                runtimeState.currentDesiredOverride(),
                forceClear = true,
            )
        }
    }

    fun updateConfig(config: DuckingConfig) {
        synchronized(stateLock) {
            applyUpdateLocked(runtimeState.updateConfig(config))
        }
    }

    fun onClientDisconnect() {
        synchronized(stateLock) {
            applyUpdateLocked(runtimeState.clearAllSources(), forceClear = true)
        }
    }

    fun onClientConnect() {
        synchronized(stateLock) {
            applyUpdateLocked(runtimeState.clearAllSources(), forceClear = true)
        }
    }

    fun onClientRuntimeUnload() {
        synchronized(stateLock) {
            applyUpdateLocked(
                runtimeState.clearAllSources(),
                forceClear = true,
                clearContextAfter = true,
            )
        }
    }

    @EventSubscribe
    fun onAudioSourceWrite(event: AudioSourceWriteEvent) {
        if (!event.source.canHear() || !event.source.isActivated()) return
        synchronized(stateLock) {
            applyUpdateLocked(runtimeState.onSourceBecameAudible(event.source))
        }
    }

    @EventSubscribe
    fun onAudioSourceReset(event: AudioSourceResetEvent) {
        synchronized(stateLock) {
            applyUpdateLocked(runtimeState.onSourceBecameInactive(event.source))
        }
    }

    private fun applyUpdateLocked(
        update: DuckingOverrideUpdate,
        forceClear: Boolean = false,
        clearContextAfter: Boolean = false,
    ) {
        val desiredOverride = when (update) {
            is DuckingOverrideUpdate.Changed -> update.override
            DuckingOverrideUpdate.NoChange -> {
                if (!forceClear) return
                null
            }
        }
        applyDesiredOverrideLocked(desiredOverride, forceClear, clearContextAfter)
    }

    private fun applyDesiredOverrideLocked(
        desiredOverride: ClientVolumeOverride?,
        forceClear: Boolean = false,
        clearContextAfter: Boolean = false,
    ) {
        val playbackService = clientContext?.clientPlaybackService
        if (playbackService != null) {
            if (desiredOverride != null) {
                playbackService.setTransientVolumeOverride(OWNER_ID, desiredOverride)
            } else if (forceClear || clientContext != null) {
                playbackService.clearTransientVolumeOverride(OWNER_ID)
            }
        }
        if (clearContextAfter) {
            clientContext = null
        }
    }
}
