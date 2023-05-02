package me.mkbaka.executableblock.internal.trigger

import me.mkbaka.executableblock.api.block.Registerable
import me.mkbaka.executableblock.api.block.Trigger
import org.bukkit.event.Event
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

object TriggerManager : Registerable<BukkitTrigger>() {

    val eventToTrigger = ConcurrentHashMap<Class<out Event>, BukkitTrigger>()

    override val root: KClass<BukkitTrigger>
        get() = BukkitTrigger::class

    fun keyToTrigger(key: String): Trigger<Event> {
        return registers.entries.find { it.key == key }?.value ?: error("错误的 trigger 类型 $key")
    }

}