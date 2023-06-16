package me.mkbaka.executableblock.internal.hook.nashorn

import me.mkbaka.executableblock.internal.hook.nashorn.NashornHooker.Companion.inst
import java.io.Reader
import javax.script.Invocable
import javax.script.ScriptEngine

/**
 * 预编译脚本
 * @author Neige
 */
open class CompiledScript {

    /**
     * 获取已编译脚本
     */
    private val compiledScript: javax.script.CompiledScript

    /**
     * 获取该脚本对应的ScriptEngine
     */
    val scriptEngine: ScriptEngine

    /**
     * 编译js脚本并进行包装, 便于调用其中的指定函数
     *
     * @property reader js脚本文件
     * @constructor 编译js脚本并进行包装
     */
    constructor(reader: Reader) {
        scriptEngine = inst.getNashornEngine()
        loadLib()
        compiledScript = inst.compile(scriptEngine, reader)
        magicFunction()
    }

    /**
     * 编译js脚本并进行包装, 便于调用其中的指定函数
     *
     * @property script js脚本文本
     * @constructor 编译js脚本并进行包装
     */
    constructor(script: String) {
        scriptEngine = inst.getNashornEngine()
        loadLib()
        compiledScript = inst.compile(scriptEngine, script)
        magicFunction()
    }

    constructor(script: String, engine: ScriptEngine) {
        scriptEngine = engine
        compiledScript = inst.compile(engine, script)
    }

    open fun loadLib() {}

    /**
     * 执行脚本中的指定函数
     *
     * @param function 函数名
     * @param map 传入的默认对象
     * @param args 传入对应方法的参数
     * @return 解析值
     */
    fun invoke(function: String, map: Map<String, Any>?, vararg args: Any): Any? {
        return inst.invoke(this, function, map, *args)
    }

    fun eval(map: Map<String, Any>): Any? {
        return inst.eval(compiledScript, map)
    }

    /**
     * 执行脚本中的指定函数
     *
     * @param function 函数名
     * @param args 传入对应方法的参数
     * @return 解析值
     */
    fun simpleInvoke(function: String, vararg args: Any?): Any? {
        return (scriptEngine as Invocable).invokeFunction(function, *args)
    }

    /**
     * 此段代码用于解决js脚本的高并发调用问题, 只可意会不可言传
     */
    private fun magicFunction() {
        compiledScript.eval()
        scriptEngine.eval(
            """
            function NeigeItemsNumberOne() {}
            NeigeItemsNumberOne.prototype = this
            function newObject() { return new NeigeItemsNumberOne() }
        """
        )
    }
}