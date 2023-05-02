package me.mkbaka.executableblock.internal.executes.impl

import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.executes.Execute
import org.bukkit.command.CommandSender
import taboolib.common5.cbool
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions

@AutoRegister("ke", alias = ["kether", "ks"])
object KetherImpl : Execute() {

    override fun eval(script: String, sender: CommandSender, args: HashMap<String, Any>): Boolean {
        return try {
            KetherShell.eval(
                script, ScriptOptions.new {
                    sender(sender)
                    vars(args)
                    namespace(listOf("ExecutableBlock"))
                }
            ).get().cbool
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }

}