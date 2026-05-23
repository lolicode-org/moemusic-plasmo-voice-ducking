package org.lolicode.moemusic.pvducking

import org.lolicode.moemusic.api.plugin.Plugin
import org.lolicode.moemusic.api.plugin.PluginProvider
import su.plo.voice.api.addon.ClientAddonsLoader

class MoeMusicPlasmoVoiceDuckingProvider: PluginProvider {
    override fun plugins(): Iterable<Plugin> {
        ClientAddonsLoader.load(PvDuckingAddon)
        return listOf(MoeMusicPlasmoVoiceDuckingPlugin)
    }
}
