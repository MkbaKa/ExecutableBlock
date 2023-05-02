package me.mkbaka.executableblock.internal.executes.impl

import me.mkbaka.executableblock.ExecutableBlock
import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.executes.Execute
import me.mkbaka.executableblock.internal.extension.cooldown.CooldownManager
import me.mkbaka.executableblock.internal.hook.nashorn.NashornHook
import me.mkbaka.executableblock.internal.hook.region.RegionManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common5.cbool
import javax.script.SimpleBindings

@AutoRegister("js", alias = ["javascript"])
object JavaScriptImpl : Execute() {

    private val bindings = hashMapOf(
        "Bukkit" to Bukkit.getServer(),
        "plugin" to ExecutableBlock.plugin,
        "CooldownManager" to CooldownManager,
        "RegionManager" to RegionManager
    )

    override fun eval(script: String, sender: CommandSender, args: HashMap<String, Any>): Boolean {
        return try {
            NashornHook.inst.compile(script).eval(SimpleBindings(args.apply {
                this["player"] = sender
                putAll(bindings)
            })).cbool
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }

}