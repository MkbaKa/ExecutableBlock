package me.mkbaka.executableblock.internal.executes.impl

import me.mkbaka.executableblock.ExecutableBlock
import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.executes.Execute
import me.mkbaka.executableblock.internal.extension.cooldown.CooldownManager
import me.mkbaka.executableblock.internal.hook.nashorn.CompiledScriptFactory
import me.mkbaka.executableblock.internal.hook.region.RegionManager
import me.mkbaka.executableblock.internal.utils.ClassUtil
import me.mkbaka.executableblock.internal.utils.FileUtil
import me.mkbaka.executableblock.internal.utils.ItemUtil
import me.mkbaka.executableblock.internal.utils.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

@AutoRegister("js", alias = ["javascript"])
object JavaScriptImpl : Execute {

    private val bindings = hashMapOf(
        "Bukkit" to Bukkit.getServer(),
        "plugin" to ExecutableBlock.plugin,
        "CooldownManager" to CooldownManager,
        "RegionManager" to RegionManager,
        "Util" to Util,
        "FileUtil" to FileUtil,
        "ItemUtil" to ItemUtil,
        "ClassUtil" to ClassUtil
    )

    override fun eval(script: String, sender: CommandSender, args: HashMap<String, Any>): Boolean {
        val result = try {
            evalScript(script, sender, args)
        } catch (e: Throwable) {
            false
        }
        return when (result) {
            is Boolean -> result
            null -> true
            else -> false
        }
    }

    override fun evalScript(script: String, sender: CommandSender, args: HashMap<String, Any>): Any? {
        return try {
            CompiledScriptFactory.compiledCondition(script).eval(args.apply {
                this["player"] = sender
                putAll(bindings)
            })
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun result(script: String, sender: CommandSender?, args: HashMap<String, Any>): Any? {
        return try {
            CompiledScriptFactory.compiled(script).invoke("invoke", map = args.apply {
                putAll(bindings)
                sender?.let { put("player", it) }
            })
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

}