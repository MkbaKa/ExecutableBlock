package me.mkbaka.executableblock.internal.executes.impl

import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.executes.Execute
import org.bukkit.command.CommandSender
import taboolib.common5.cbool
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions

@AutoRegister("ke", alias = ["kether", "ks"])
object KetherImpl : Execute {

    override fun eval(script: String, sender: CommandSender, args: HashMap<String, Any>): Boolean {
        return result(script, sender, args).cbool
    }

    override fun evalScript(script: String, sender: CommandSender, args: HashMap<String, Any>): Any? {
        return result(script, sender, args)
    }

    override fun result(script: String, sender: CommandSender?, args: HashMap<String, Any>): Any? {
        return try {
            KetherShell.eval(
                script, ScriptOptions.new {
                    sender?.let { sender(it) }
                    vars(args)
                    namespace(listOf("ExecutableBlock"))
                }
            ).getNow(null)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}