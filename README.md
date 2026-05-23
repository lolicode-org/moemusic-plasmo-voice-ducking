# MoeMusic Plasmo Voice Ducking Plugin

[简体中文](./README_zh.md) | English

A standalone, client-side only plugin for [MoeMusic](https://github.com/lolicode-org/MoeMusic) that automatically lowers (ducks) MoeMusic's volume when other players speak via [Plasmo Voice](https://github.com/plasmoapp/plasmo-voice).

## Features

- **Automatic Ducking**: Lowers background music volume when other players start talking in Minecraft.
- **Seamless Recovery**: Automatically restores the music to its configured volume once players finish speaking.
- **Customizable Ducking Modes**: Supports limiting volume to a hard cap or scaling it proportionally to your current music volume.

---

## Installation

This plugin runs entirely on the client. Do **not** install it on a dedicated server.

> [!WARNING]
> This plugin requires both **MoeMusic** and **Plasmo Voice** to be installed on your client. If either mod is missing, the client will fail to start and throw a `NoClassDefFoundError` during class loading.

1. Obtain the plugin JAR file (e.g., `moemusic-plasmo-voice-ducking-<version>.jar`).
   - *If compiling from source:* Run `./gradlew build` in the project root. The compiled JAR will be located in `build/libs/`.
2. Place the JAR file into the `config/moemusic/plugins/` directory of your Minecraft client instance.
3. Start your Minecraft client.

---

## Configuration

> [!TIP]
> These are for advanced users who like to edit configuration files directly. You can use the in-game configuration GUI provided by MoeMusic to adjust these settings.

After launching the game once with the plugin installed, a configuration file will be auto-generated at:
`config/moemusic/plugin-configs/moemusic_pv_ducking.toml`

### Configuration Options

| Option | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `enabled` | Boolean | `true` | Enable or disable the audio ducking behavior. |
| `volume_mode` | String | `"PERCENT_OF_CURRENT"` | The mode used to lower volume. See details below. |
| `value` | Integer | `25` | Ducking target/scaling value (from `0` to `100`). |

#### Volume Modes
- `"PERCENT_OF_CURRENT"`: Proportional scaling (Default). Scales the current music volume by the `value` percentage (e.g., if set to `25`, volume is reduced to 25% of its current level).
- `"FIXED_PERCENT"`: Fixed cap. Caps the music volume at the specified `value` percentage. If the current volume is already lower than this cap, it remains unchanged.

### Example Configuration

```toml
enabled = true
volume_mode = "PERCENT_OF_CURRENT"
value = 25
```

---

## License

This project is licensed under the GNU Affero General Public License v3.0 or later (AGPL-3.0-or-later). See the [LICENSE](./LICENSE) file for details.
