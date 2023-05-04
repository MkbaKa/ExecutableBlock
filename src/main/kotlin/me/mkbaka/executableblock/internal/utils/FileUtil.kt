package me.mkbaka.executableblock.internal.utils

import me.mkbaka.executableblock.internal.block.Executor
import me.mkbaka.executableblock.internal.settings.SettingManager
import me.mkbaka.executableblock.internal.trigger.BukkitTrigger
import me.mkbaka.executableblock.internal.trigger.TriggerManager
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration
import java.io.File

object FileUtil {

    fun File.executeSubFiles(callback: (File) -> Unit) {
        listFiles()?.forEach {
            if (it.isDirectory) it.executeSubFiles(callback)
            else callback(it)
        }
    }

    fun File.loadSections(callback: (config: Configuration, section: String) -> Unit) {
        val config = Configuration.loadFromFile(this)
        config.getKeys(false).forEach {
            callback(config, it)
        }
    }

    fun ConfigurationSection.toGlobal(): Executor {
        val key = getString("extend") ?: return Executor(this)
        val exec = SettingManager.getExecutor(key) ?: error("未知的 executes 节点名 $key, 请检查相关配置.")
        return Executor(exec.root, getTrigger())
    }

    fun ConfigurationSection.getTrigger(): BukkitTrigger {
        return TriggerManager.keyToTrigger(getString("trigger")!!.lowercase()) as BukkitTrigger
    }

}