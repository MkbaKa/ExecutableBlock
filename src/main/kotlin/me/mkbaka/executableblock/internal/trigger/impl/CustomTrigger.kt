package me.mkbaka.executableblock.internal.trigger.impl

import me.mkbaka.executableblock.internal.executes.impl.JavaScriptImpl
import me.mkbaka.executableblock.internal.trigger.BukkitEventAdapter
import me.mkbaka.executableblock.internal.trigger.BukkitTrigger
import me.mkbaka.executableblock.internal.utils.ClassUtil.toBukkitEvent
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import taboolib.module.configuration.Configuration

class CustomTrigger(val config: Configuration, val section: String) : BukkitTrigger() {

    override val eventClass: Class<out Event>
        get() = config.getString("$section.bind")!!.toBukkitEvent()

    override fun getBlock(event: BukkitEventAdapter): Block? {
        if (!config.isString("$section.override.getBlock")) return super.getBlock(event)
        return JavaScriptImpl.result(
            config.getString("$section.override.getBlock")!!,
            args = hashMapOf("event" to event.event)
        ) as? Block
    }

    override fun getPlayer(event: BukkitEventAdapter): Player? {
        if (!config.isString("$section.override.getPlayer")) return super.getPlayer(event)
        return JavaScriptImpl.result(
            config.getString("$section.override.getPlayer")!!,
            args = hashMapOf("event" to event.event)
        ) as? Player
    }

    override fun condition(event: BukkitEventAdapter): Boolean {
        if (!config.isString("$section.override.condition")) return super.condition(event)
        var script = config.getString("$section.override.condition")!!
        if (script.contains("super.condition()")) script =
            script.replace("super.condition()", super.condition(event).toString())
        return JavaScriptImpl.eval(
            script,
            getPlayer(event)!!,
            args = hashMapOf("event" to event.event)
        )
    }

}