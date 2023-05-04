package me.mkbaka.executableblock.internal.executes.impl

import me.mkbaka.executableblock.ExecutableBlock
import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.executes.Execute
import me.mkbaka.executableblock.internal.extension.cooldown.CooldownManager
import me.mkbaka.executableblock.internal.hook.nashorn.NashornHook
import me.mkbaka.executableblock.internal.hook.region.RegionManager
import me.mkbaka.executableblock.internal.utils.FileUtil
import me.mkbaka.executableblock.internal.utils.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common5.cbool
import javax.script.SimpleBindings

@AutoRegister("js", alias = ["javascript"])
object JavaScriptImpl : Execute {

    private val bindings = hashMapOf(
        "Bukkit" to Bukkit.getServer(),
        "plugin" to ExecutableBlock.plugin,
        "CooldownManager" to CooldownManager,
        "RegionManager" to RegionManager,
        "Util" to Util,
        "FileUtil" to FileUtil
    )

    override fun eval(script: String, sender: CommandSender?, args: HashMap<String, Any>): Boolean {
        return result(script, sender, args).cbool
    }

    override fun result(script: String, sender: CommandSender?, args: HashMap<String, Any>): Any? {
        return try {
            NashornHook.inst.compile(script).eval(SimpleBindings(args.apply {
                sender?.let { this["player"] = it }
                putAll(bindings)
            }))
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

}