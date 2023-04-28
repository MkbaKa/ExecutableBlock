package me.mkbaka.executableblock.api.block

import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.isSubclassOf

abstract class Registerable<T : Any> {

    protected val registers = ConcurrentHashMap<String, T>()

    abstract val root: KClass<T>

    fun register(clazz: KClass<*>, callback: (T) -> Unit = {}) {
        if (clazz.isSubclassOf(root)) {
            val inst = clazz.objectInstance as? T ?: return
            val register = clazz.findAnnotations(AutoRegister::class).first()

            register.alias.plus(register.name).forEach {
                registers[it] = inst
            }

            callback(inst)
        }
    }

}