package me.mkbaka.executableblock.internal.hook.nashorn.impl

import me.mkbaka.executableblock.internal.hook.nashorn.NashornHooker
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import java.io.Reader
import javax.script.*

@RuntimeDependencies(
    RuntimeDependency(
        "!org.openjdk.nashorn:nashorn-core:15.4",
        test = "!jdk.nashorn.api.scripting.NashornScriptEngineFactory"
    )
)
/**
 * openjdk nashorn挂钩
 *
 * @constructor 启用openjdk nashorn挂钩
 */
class NashornHookerImpl : NashornHooker() {
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

    override fun invoke(compiledScript: me.mkbaka.executableblock.internal.hook.nashorn.CompiledScript, function: String, map: Map<String, Any>?, vararg args: Any): Any? {
        val newObject: ScriptObjectMirror = (compiledScript.scriptEngine as Invocable).invokeFunction("newObject") as ScriptObjectMirror
        map?.forEach { (key, value) -> newObject[key] = value }
        return newObject.callMember(function, *args)
    }

    override fun eval(compiledScript: CompiledScript, map: Map<String, Any>): Boolean {
        val result = try {
            compiledScript.eval(SimpleBindings(map))
        } catch (e: Throwable) {
            false
        }
        return when (result) {
            is Boolean -> result
            null -> true
            else -> false
        }
    }

    override fun isFunction(engine: ScriptEngine, func: Any?): Boolean {
        if (func is ScriptObjectMirror && func.isFunction) {
            return true
        }
        return false
    }
}