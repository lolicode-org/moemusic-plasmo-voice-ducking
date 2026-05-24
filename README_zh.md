# MoeMusic Plasmo Voice 语音避让插件

简体中文 | [English](./README.md)

本插件是为 [MoeMusic](https://github.com/lolicode-org/MoeMusic) 播放器/模组设计的**仅客户端**音量闪避扩展。当听到其他玩家通过 [Plasmo Voice](https://plasmoverse.com/) 语音聊天时，本插件能自动降低 MoeMusic 的播放音量，并在说话结束后恢复正常。

## 功能特性

- **自动语音避让**：当其他玩家通过语音说话时，自动降低背景音乐音量，以防盖过人声。
- **平滑恢复音量**：玩家停止说话后，自动将背景音乐恢复至常规大小。
- **自定义避让模式**：支持直接限制为固定最大音量上限，或按比例缩放原音乐音量。

---

## 安装方法

本插件仅需在客户端安装。请**勿**将其放入独立服务端（Dedicated Server）。

1. 获取插件的 JAR 文件（例如 `moemusic-plasmo-voice-ducking-<version>.jar`）。
   - *若从源码编译*：在项目根目录下运行 `./gradlew jar`。编译生成的 JAR 文件位于 `build/libs/` 目录下。
2. 将 JAR 文件放入客户端实例的 `config/moemusic/plugins/` 目录中。
3. 启动 Minecraft 客户端即可。

---

## 配置说明

> [!TIP]
> 以下配置适用于喜欢直接编辑配置文件的用户。你可以直接使用 MoeMusic 提供的游戏内配置界面来调整这些设置。

插件首次加载成功后，会在以下路径自动生成配置文件：
`config/moemusic/plugin-configs/moemusic_pv_ducking.toml`

### 配置项

| 配置键名          | 类型  | 默认值                    | 说明                       |
|:--------------|:----|:-----------------------|:-------------------------|
| `enabled`     | 布尔值 | `true`                 | 是否启用语音避让功能。              |
| `volume_mode` | 字符串 | `"PERCENT_OF_CURRENT"` | 音量降低模式。详情见下文。            |
| `value`       | 整数  | `25`                   | 避让后音量数值（范围 `0` 到 `100`）。 |

#### 音量降低模式
- `"PERCENT_OF_CURRENT"`：配置音量百分比（默认）。将当前的播放音量缩放至其设定的百分比大小（例如设为 `25`，则音量降低至主音量的 25%）。
- `"FIXED_PERCENT"`：固定音量上限。将播放音量限制在该配置百分比。若当前音乐音量本身低于该上限，则保持原样。

### 示例配置

```toml
enabled = true
volume_mode = "PERCENT_OF_CURRENT"
value = 25
```

---

## 开源许可证

本项目采用 GNU Affero General Public License v3.0 or later (AGPL-3.0-or-later) 开源许可证。详情请参阅 [LICENSE](./LICENSE) file。
