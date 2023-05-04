package me.mkbaka.executableblock.internal.settings

import me.mkbaka.executableblock.api.block.Registerable
import me.mkbaka.executableblock.internal.block.Executor
import me.mkbaka.executableblock.internal.trigger.BukkitTrigger
import kotlin.reflect.KClass

/**
 * 配置管理
 * @author Mkbakaa
 * @date 2023/05/04
 */
object SettingManager : Registerable<Configurations<*, *>>() {

    override val root: KClass<Configurations<*, *>>
        get() = Configurations::class

    fun callReload() {
        registers.forEach { (_, config) ->
            config.callReload()
        }
    }

    fun getAllExecutorKeys(): List<String> {
        return registers["executors"]!!.keys.map { it.toString() }
    }

    fun getExecutor(key: Any): Executor? {
        return registers["executors"]!!.findValue(key) as? Executor
    }

    fun hasExecutor(key: Any): Boolean {
        return registers["executors"]!!.containsKey(key)
    }

    fun getGlobalExecutors(type: BukkitTrigger): HashMap<String, Executor>? {
        return registers["global"]!!.findValue(type) as? HashMap<String, Executor>
    }

}