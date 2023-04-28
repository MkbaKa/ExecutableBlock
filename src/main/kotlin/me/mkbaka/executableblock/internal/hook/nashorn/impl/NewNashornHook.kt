package me.mkbaka.executableblock.internal.hook.nashorn.impl

import me.mkbaka.executableblock.internal.hook.nashorn.NashornHook
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror
import taboolib.common.env.RuntimeDependency
import java.io.Reader
import javax.script.*

@RuntimeDependency(
    "!org.openjdk.nashorn:nashorn-core:15.4", test = "jdk.nashorn.api.scripting.NashornScriptEngineFactory"
)
object NewNashornHook : NashornHook() {

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