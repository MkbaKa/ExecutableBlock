package me.mkbaka.executableblock.internal.settings.impl

import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.block.Executor
import me.mkbaka.executableblock.internal.settings.Configurations
import me.mkbaka.executableblock.internal.utils.FileUtil.loadSections
import java.io.File

@AutoRegister("executors")
object Executors : Configurations<String, Executor>() {

    override val path: String
        get() = "executes"
    override val example: String
        get() = "default.yml"

    override fun onReload(file: File) {
        file.loadSections { config, section ->
            computeIfAbsent(section) {
                Executor(config.getConfigurationSection(section)!!, null)
            }
        }
    }

}