package me.mkbaka.executableblock.internal.settings

import me.mkbaka.executableblock.internal.block.ExecutableBlock
import me.mkbaka.executableblock.internal.utils.FileUtil.executeSubFiles
import taboolib.common.LifeCycle
import taboolib.common.io.newFile
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.Configuration
import java.util.concurrent.ConcurrentHashMap

object Settings {

    @Config("settings.yml")
    lateinit var settings: ConfigFile

    private val subFolder by lazy {
        newFile(getDataFolder(), "executes", create = true, folder = true)
    }

    val executes = ConcurrentHashMap<String, ExecutableBlock>()

    var updatePeriod: Long = 0

    @Awake(LifeCycle.ACTIVE)
    fun active() {
        settings.onReload {
            updatePeriod = settings.getLong("Storage.period") * 20
        }
        reload()
    }

    fun reload() {
        settings.reload()
        releaseResourceFile("executes/default.yml", replace = false)
        subFolder.executeSubFiles { file ->
            if (file.extension != "yml") return@executeSubFiles
            val config = Configuration.loadFromFile(file)
            config.getKeys(false).forEach {
                executes[it] = ExecutableBlock(config.getConfigurationSection(it)!!)
            }
        }
    }
}