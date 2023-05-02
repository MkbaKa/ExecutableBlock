package me.mkbaka.executableblock.internal.utils

import me.mkbaka.executableblock.internal.block.ExecutableBlock
import me.mkbaka.executableblock.internal.settings.Settings
import me.mkbaka.executableblock.internal.trigger.BukkitTrigger
import me.mkbaka.executableblock.internal.trigger.TriggerManager
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
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

    fun createFolder(folder: String, create: Boolean = true, callback: (File) -> Unit): ActionFile {
        return ActionFile(newFile(getDataFolder(), folder, create, folder = true), callback)
    }

    fun File.loadSections(callback: (config: Configuration, section: String) -> Unit) {
        val config = Configuration.loadFromFile(this)
        config.getKeys(false).forEach {
            callback(config, it)
        }
    }

    fun ConfigurationSection.toGlobal(): ExecutableBlock {
        val key = getString("extend") ?: return ExecutableBlock(this)
        val exec = Settings.executes[key] ?: error("未知的 executes 节点名 $key, 请检查相关配置.")
        return ExecutableBlock(exec.root, getTrigger())
    }

    fun ConfigurationSection.getTrigger(): BukkitTrigger {
        return TriggerManager.keyToTrigger(getString("trigger")!!.lowercase()) as BukkitTrigger
    }

    data class ActionFile(val file: File, val callback: (File) -> Unit)

}