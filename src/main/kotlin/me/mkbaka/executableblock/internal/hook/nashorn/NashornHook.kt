package me.mkbaka.executableblock.internal.hook.nashorn

import me.mkbaka.executableblock.internal.hook.nashorn.impl.LegacyNashornHook
import me.mkbaka.executableblock.internal.hook.nashorn.impl.NewNashornHook
import java.io.Reader
import java.util.concurrent.ConcurrentHashMap
import javax.script.Compilable
import javax.script.CompiledScript
import javax.script.ScriptEngine
import javax.script.SimpleBindings

/**
 *
 * @author Neige 我滴卡密！！！
 * @github https://github.com/ankhorg/NeigeItems-Kotlin
 */
abstract class NashornHook {

    private val compiledScripts = ConcurrentHashMap<String, CompiledScript>()

    fun getScriptEngine(): ScriptEngine {
        return getNashornEngine(arrayOf("-Dnashorn.args=--language=es6"))
    }

    fun getGlobalEngine(): ScriptEngine {
        return getNashornEngine(arrayOf("-DNashorn.args=--language=es6", "--global-per-engine"))
    }

    fun compile(reader: Reader, engine: ScriptEngine = getScriptEngine()): CompiledScript {
        return compiledScripts.computeIfAbsent(reader.readText()) {
            (engine as Compilable).compile(reader)
        }
    }

    fun compile(script: String, engine: ScriptEngine = getScriptEngine()): CompiledScript {
        return compiledScripts.computeIfAbsent(script) {
            (engine as Compilable).compile(script)
        }
    }

    fun eval(script: CompiledScript, map: Map<String, Any>): Any {
        val result = try {
            script.eval(SimpleBindings(map))
        } catch (e: Throwable) {
            false
        }
        return when (result) {
            is Boolean -> result
            null -> true
            else -> false
        }
    }

    abstract fun getNashornEngine(args: Array<String>): ScriptEngine

    abstract fun invoke(
        compileScript: me.mkbaka.executableblock.internal.hook.nashorn.CompiledScript,
        func: String,
        map: Map<String, Any?> = emptyMap(),
        vararg args: Any
    ): Any

    companion object {

        val inst by lazy {
            try {
                Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory")
                LegacyNashornHook
            } catch (e: Throwable) {
                NewNashornHook
            }
        }
        
        val globalEngine = inst.getGlobalEngine()

    }
}