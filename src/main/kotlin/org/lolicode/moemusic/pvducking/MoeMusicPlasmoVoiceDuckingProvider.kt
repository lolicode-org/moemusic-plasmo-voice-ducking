package org.lolicode.moemusic.pvducking

import org.lolicode.moemusic.api.plugin.Plugin
import org.lolicode.moemusic.api.plugin.PluginProvider
import su.plo.voice.api.addon.ClientAddonsLoader
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class MoeMusicPlasmoVoiceDuckingProvider: PluginProvider {
    override fun plugins(): Iterable<Plugin> {
        val missingClass = REQUIRED_PLASMO_VOICE_CLASSES.firstOrNull { !isClassAvailable(it) }
        if (missingClass != null) {
            LOGGER.error(
                "Plasmo Voice is not installed or incomplete ($missingClass is unavailable). " +
                    "Skipping MoeMusic Plasmo Voice Ducking initialization.",
            )
            return emptyList()
        }

        return try {
            ClientAddonsLoader.load(PvDuckingAddon)
            listOf(MoeMusicPlasmoVoiceDuckingPlugin)
        } catch (error: LinkageError) {
            LOGGER.error(
                "Plasmo Voice is not available at runtime. " +
                    "Skipping MoeMusic Plasmo Voice Ducking initialization.",
                error,
            )
            emptyList()
        } catch (error: Exception) {
            LOGGER.error(
                "Failed to initialize MoeMusic Plasmo Voice Ducking. Skipping plugin registration.",
                error,
            )
            emptyList()
        }
    }

    private fun isClassAvailable(className: String): Boolean = try {
        Class.forName(className, false, javaClass.classLoader)
        true
    } catch (_: ClassNotFoundException) {
        false
    } catch (_: LinkageError) {
        false
    }

    private companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(MoeMusicPlasmoVoiceDuckingProvider::class.java)

        private val REQUIRED_PLASMO_VOICE_CLASSES = listOf(
            "su.plo.voice.api.addon.ClientAddonsLoader",
            "su.plo.voice.api.addon.AddonInitializer",
            "su.plo.voice.api.client.PlasmoVoiceClient",
        )
    }
}
