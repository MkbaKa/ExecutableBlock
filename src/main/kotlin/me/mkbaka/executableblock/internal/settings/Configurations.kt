package me.mkbaka.executableblock.internal.settings

import me.mkbaka.executableblock.internal.utils.FileUtil.executeSubFiles
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * 配置处理
 * @author Mkbakaa
 * @date 2023/05/04
 */
abstract class Configurations<K, V> : ConcurrentHashMap<K, V>() {

    abstract val path: String
    open fun preReload() {}

    abstract fun onReload(file: File)

    val folder = newFile(getDataFolder(), path, folder = true, create = true)

    fun releaseResource(file: String) {
        releaseResourceFile(file, replace = false)
    }

    fun callReload() {
        clear()
        preReload()
        folder.executeSubFiles {
            onReload(it)
        }
    }

    open fun findValue(key: Any): V? {
        return entries.firstOrNull { it.key == key }?.value
    }

    init {
        newFile(getDataFolder(), path, folder = true, create = true)
    }

}