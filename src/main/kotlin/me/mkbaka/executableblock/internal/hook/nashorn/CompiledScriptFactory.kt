package me.mkbaka.executableblock.internal.hook.nashorn

import java.util.concurrent.ConcurrentHashMap

/**
 * 预编译管理
 */
object CompiledScriptFactory {

    private val compiledScript = ConcurrentHashMap<Int, CompiledScript>()
    private val globalEngine = NashornHooker.inst.getGlobalEngine()

    fun compiledCondition(script: String): CompiledScript {
        return compiledScript.computeIfAbsent(script.hashCode()) {
            CompiledScript(script, globalEngine)
        }
    }

    fun compiled(script: String): CompiledScript {
        return compiledScript.computeIfAbsent(script.hashCode()) {
            ScriptExpansion(script)
        }
    }

    fun reload() {
        compiledScript.clear()
    }

}