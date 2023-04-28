package me.mkbaka.executableblock.internal.hook.nashorn

import java.io.Reader

open class CompiledScript {

    private val hook by lazy {
        NashornHook.inst
    }

    private val compiledScript: javax.script.CompiledScript

    val scriptEngine = hook.getScriptEngine()

    constructor(reader: Reader) {
        loadLib()
        compiledScript = hook.compile(reader, scriptEngine)
        magicFunction()
    }

    constructor(script: String) {
        loadLib()
        compiledScript = hook.compile(script, scriptEngine)
        magicFunction()
    }

    open fun loadLib() {
        scriptEngine.eval("""
            const Bukkit = Packages.org.bukkit.Bukkit
            const plugin = Packages.me.mkbaka.executableblock.ExecutableBlock.INSTANCE.plugin
            
            const ItemStack = Packages.org.bukkit.inventory.ItemStack
            const Material = Packages.org.bukkit.Material
            
            const NMSI18N = Packages.me.mkbaka.executableblock.taboolib.module.nms.NMSI18nKt
            const PlaceholderAPI = Packages.me.clip.placeholderapi.PlaceholderAPI
            
            const papi = function(player, str) {
                return PlaceholderAPI.setPlaceholders(player, str)
            }
            
            const getName = function(item, player) {
                return NMSI18N.getName(item, player)
            }
        """.trimIndent()
        )
    }

    fun invoke(func: String, map: Map<String, Any> = emptyMap(), vararg args: Any): Any {
        return hook.invoke(this, func, map, args)
    }

    fun eval(map: Map<String, Any>): Any {
        return hook.eval(compiledScript, map)
    }

    /**
     * 此段代码用于解决js脚本的高并发调用问题, 只可意会不可言传
     * @author Neige
     *
     ****** 卡密の传家之宝
     */
    private fun magicFunction() {
        scriptEngine.eval(
            """
            function NeigeItemsNumberOne() {}
            NeigeItemsNumberOne.prototype = this
            function newObject() { return new NeigeItemsNumberOne() }
        """
        )
    }

}