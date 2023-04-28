package me.mkbaka.executableblock.internal.hook.nashorn.impl

import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import jdk.nashorn.api.scripting.ScriptObjectMirror
import me.mkbaka.executableblock.internal.hook.nashorn.NashornHook
import javax.script.Invocable
import javax.script.ScriptEngine

object LegacyNashornHook : NashornHook() {

    override fun getNashornEngine(args: Array<String>): ScriptEngine {
        return NashornScriptEngineFactory().getScriptEngine(args, this::class.java.classLoader)
    }

    override fun invoke(
        compileScript: me.mkbaka.executableblock.internal.hook.nashorn.CompiledScript,
        func: String,
        map: Map<String, Any?>,
        vararg args: Any
    ): Any {
        return ((compileScript.scriptEngine as Invocable).invokeFunction("newObject") as ScriptObjectMirror).run {
            putAll(map)
            callMember(func, args)
        }
    }

}