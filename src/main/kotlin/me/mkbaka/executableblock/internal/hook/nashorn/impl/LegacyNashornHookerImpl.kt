package me.mkbaka.executableblock.internal.hook.nashorn.impl

import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import jdk.nashorn.api.scripting.ScriptObjectMirror
import me.mkbaka.executableblock.internal.hook.nashorn.NashornHooker
import java.io.Reader
import javax.script.*

/**
 * jdk自带nashorn挂钩
 *
 * @constructor 启用jdk自带nashorn挂钩
 */
class LegacyNashornHookerImpl : NashornHooker() {

    override fun getNashornEngine(): ScriptEngine {
        return getNashornEngine(arrayOf("-Dnashorn.args=--language=es6"))
    }

    override fun getGlobalEngine(): ScriptEngine {
        return getNashornEngine(arrayOf("-Dnashorn.args=--language=es6", "--global-per-engine"))
    }

    override fun getNashornEngine(args: Array<String>): ScriptEngine {
        return NashornScriptEngineFactory().getScriptEngine(args, this::class.java.classLoader)
    }

    override fun compile(string: String): CompiledScript {
        return (getNashornEngine() as Compilable).compile(string)
    }

    override fun compile(reader: Reader): CompiledScript {
        return (getNashornEngine() as Compilable).compile(reader)
    }

    override fun invoke(
        compiledScript: me.mkbaka.executableblock.internal.hook.nashorn.CompiledScript,
        function: String,
        map: Map<String, Any>?,
        vararg args: Any
    ): Any? {
        val newObject: ScriptObjectMirror =
            (compiledScript.scriptEngine as Invocable).invokeFunction("newObject") as ScriptObjectMirror
        map?.forEach { (key, value) -> newObject[key] = value }
        return newObject.callMember(function, *args)
    }

    override fun eval(
        compiledScript: CompiledScript,
        map: Map<String, Any>
    ): Any? {
       return compiledScript.eval(SimpleBindings(map))
    }

    override fun isFunction(engine: ScriptEngine, func: Any?): Boolean {
        if (func is ScriptObjectMirror && func.isFunction) {
            return true
        }
        return false
    }
}