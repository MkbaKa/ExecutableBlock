package me.mkbaka.executableblock.internal.trigger

import me.mkbaka.executableblock.api.block.Registerable
import me.mkbaka.executableblock.api.block.Trigger
import me.mkbaka.executableblock.internal.block.BlockManager
import me.mkbaka.executableblock.internal.trigger.impl.CustomTrigger
import org.bukkit.event.Event
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.unregisterListener
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

object TriggerManager : Registerable<BukkitTrigger>() {

    private val customTriggers = ConcurrentHashMap<BukkitTrigger, ProxyListener>()

    val eventToTrigger = ConcurrentHashMap<Class<out Event>, BukkitTrigger>()

    override val root: KClass<BukkitTrigger>
        get() = BukkitTrigger::class

    /**
     * 将 String 转换为 Trigger
     * @param [key] key
     * @return [Trigger<Event>]
     */
    fun keyToTrigger(key: String): Trigger<Event> {
        return registers.entries.find { it.key == key }?.value ?: error("错误的 trigger 类型 $key")
    }

    /**
     * 将触发器绑定到事件
     * @param [trigger] 触发器
     */
    fun bindToEvent(trigger: BukkitTrigger) {
        val listener = registerBukkitListener(
            trigger.eventClass, EventPriority.LOWEST, true
        ) {
            val adapter = BukkitEventAdapter(it)
            if (!trigger.condition(adapter)) return@registerBukkitListener BlockManager.callExecute(null, adapter)

            trigger.call(adapter)
        }
        if (trigger is CustomTrigger) customTriggers[trigger] = listener
    }

    /**
     * 注销自定义触发器监听
     * 不注销会导致每次重载都多注册于一次监听...
     */
    fun unRegisterCustomListener() {
        customTriggers.forEach { (_, listener) ->
            unregisterListener(listener)
        }
        customTriggers.clear()
    }

}