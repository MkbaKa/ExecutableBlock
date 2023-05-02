package me.mkbaka.executableblock.internal.settings

import me.mkbaka.executableblock.api.block.Trigger
import me.mkbaka.executableblock.internal.block.ExecutableBlock
import me.mkbaka.executableblock.internal.trigger.TriggerManager
import me.mkbaka.executableblock.internal.utils.FileUtil.createFolder
import me.mkbaka.executableblock.internal.utils.FileUtil.executeSubFiles
import me.mkbaka.executableblock.internal.utils.FileUtil.loadSections
import me.mkbaka.executableblock.internal.utils.FileUtil.toGlobal
import org.bukkit.event.Event
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import java.util.concurrent.ConcurrentHashMap

object Settings {

    @Config("settings.yml")
    lateinit var settings: ConfigFile

    private val loaders by lazy {
        listOf(
            createFolder("executes") { file ->
                file.loadSections { config, section ->
                    executes[section] = ExecutableBlock(config.getConfigurationSection(section)!!, null)
                }
            },
            createFolder("global") { file ->
                file.loadSections { config, section ->
                    globalExecutes.computeIfAbsent(TriggerManager.keyToTrigger(config.getString("$section.trigger")!!)) {
                        hashMapOf()
                    }[section] = config.getConfigurationSection(section)!!.toGlobal()
                }
            }
        )
    }

    val executes = ConcurrentHashMap<String, ExecutableBlock>()
    val globalExecutes = ConcurrentHashMap<Trigger<Event>, HashMap<String, ExecutableBlock>>()

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
        releaseResourceFile("global/example.yml", replace = false)
        executes.clear()
        globalExecutes.clear()
        loaders.forEach { af ->
            af.file.executeSubFiles { file ->
                if (file.extension != "yml") return@executeSubFiles
                af.callback(file)
            }
        }
    }
}