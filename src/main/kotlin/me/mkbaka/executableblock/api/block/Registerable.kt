package me.mkbaka.executableblock.api.block

import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.isSubclassOf

abstract class Registerable<T : Any> {

    val registers = ConcurrentHashMap<String, T>()

    abstract val root: KClass<T>
    open fun register(clazz: KClass<*>, callback: (T) -> Unit = {}) {
        if (clazz.isSubclassOf(root)) {
            val inst = clazz.objectInstance as? T ?: return
            val register = clazz.findAnnotations(AutoRegister::class).first()

            register(register.alias.plus(register.name), inst)

            callback(inst)
        }
    }

    fun register(names: Collection<String>, inst: T, callback: (T) -> Unit = {}) {
        names.forEach {
            if (registers.containsKey(it)) registers.remove(it)
            registers[it] = inst
        }
        callback(inst)
    }

    fun register(names: Array<String>, inst: T, callback: (T) -> Unit = {}) {
        names.forEach {
            if (registers.containsKey(it)) registers.remove(it)
            registers[it] = inst
        }
        callback(inst)
    }

}