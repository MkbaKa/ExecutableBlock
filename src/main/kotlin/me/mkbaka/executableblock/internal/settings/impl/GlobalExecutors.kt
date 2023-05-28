package me.mkbaka.executableblock.internal.settings.impl

import me.mkbaka.executableblock.api.block.Trigger
import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.block.Executor
import me.mkbaka.executableblock.internal.settings.Configurations
import me.mkbaka.executableblock.internal.trigger.TriggerManager
import me.mkbaka.executableblock.internal.utils.FileUtil.loadSections
import me.mkbaka.executableblock.internal.utils.FileUtil.toGlobal
import java.io.File

@AutoRegister("global")
object GlobalExecutors : Configurations<Trigger<*>, HashMap<String, Executor>>() {

    override val path: String
        get() = "global"

    override fun onReload(file: File) {
        file.loadSections { config, section ->
            computeIfAbsent(TriggerManager.keyToTrigger(config.getString("$section.trigger")!!)) {
                hashMapOf()
            }[section] = config.getConfigurationSection(section)!!.toGlobal()
        }
    }

}