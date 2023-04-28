package me.mkbaka.executableblock.internal.trigger

import me.mkbaka.executableblock.api.block.Registerable
import me.mkbaka.executableblock.api.block.Trigger
import kotlin.reflect.KClass

object TriggerManager : Registerable<Trigger>() {

    override val root: KClass<Trigger>
        get() = Trigger::class

    fun keyToTrigger(key: String): Trigger {
        return registers.entries.find { it.key == key }?.value ?: error("错误的 trigger 类型 $key")
    }

}