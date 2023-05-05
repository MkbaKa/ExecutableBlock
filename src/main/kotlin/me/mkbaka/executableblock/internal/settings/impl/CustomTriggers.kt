package me.mkbaka.executableblock.internal.settings.impl

import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.settings.Configurations
import me.mkbaka.executableblock.internal.trigger.BukkitTrigger
import me.mkbaka.executableblock.internal.trigger.TriggerManager
import me.mkbaka.executableblock.internal.trigger.TriggerManager.eventToTrigger
import me.mkbaka.executableblock.internal.trigger.impl.CustomTrigger
import me.mkbaka.executableblock.internal.utils.FileUtil.loadSections
import java.io.File

@AutoRegister("CustomTrigger")
object CustomTriggers : Configurations<String, BukkitTrigger>() {

    override val path: String
        get() = "triggers"
    override val examples: List<String>
        get() = listOf("$path/example.yml", "global/CustomTrigger.yml")

    override fun preReload() {
        TriggerManager.unRegisterCustomListener()
    }

    override fun onReload(file: File) {
        file.loadSections { config, section ->
            val trigger = CustomTrigger(config, section)

            TriggerManager.register(
                config.getStringList("$section.triggers"),
                trigger
            ) { it.bindToEvent() }

            if (eventToTrigger.containsKey(trigger.eventClass)) eventToTrigger.remove(trigger.eventClass)
            eventToTrigger[trigger.eventClass] = trigger

            config.getStringList("$section.triggers").forEach {
                computeIfAbsent(it) { trigger }
            }
        }
    }

}