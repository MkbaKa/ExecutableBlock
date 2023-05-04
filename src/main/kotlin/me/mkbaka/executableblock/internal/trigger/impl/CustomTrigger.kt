package me.mkbaka.executableblock.internal.trigger.impl

import me.mkbaka.executableblock.api.block.EventAdapter
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

    override fun getBlock(event: EventAdapter): Block? {
        if (event !is BukkitEventAdapter) return null
        if (!config.isString("$section.override.getBlock")) return super.getBlock(event)
        return JavaScriptImpl.result(
            config.getString("$section.override.getBlock")!!,
            args = hashMapOf("event" to event.event)
        ) as? Block
    }

    override fun getPlayer(event: EventAdapter): Player? {
        if (event !is BukkitEventAdapter) return null
        if (!config.isString("$section.override.getPlayer")) return super.getPlayer(event)
        return JavaScriptImpl.result(
            config.getString("$section.override.getPlayer")!!,
            args = hashMapOf("event" to event.event)
        ) as? Player
    }

    override fun condition(event: EventAdapter): Boolean {
        if (event !is BukkitEventAdapter) return false
        if (!config.isString("$section.override.condition")) return super.condition(event)
        return JavaScriptImpl.eval(
            config.getString("$section.override.condition")!!,
            getPlayer(event),
            args = hashMapOf("event" to event.event)
        )
    }

}