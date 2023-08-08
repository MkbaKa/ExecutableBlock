package me.mkbaka.executableblock.internal.executes.impl

import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.executes.Execute
import me.mkbaka.executableblock.internal.executes.impl.KetherImpl.namespaces
import net.minecraft.server.v1_14_R1.Sensor
import org.bukkit.command.CommandSender
import taboolib.common5.cbool
import taboolib.module.kether.KetherShell
import taboolib.module.kether.ScriptOptions
import taboolib.module.kether.runKether

@AutoRegister("ke", alias = ["kether", "ks"])
object KetherImpl : Execute {

    val namespaces by lazy { listOf("ExecutableBlock") }

    override fun eval(script: String, sender: CommandSender, args: HashMap<String, Any>): Boolean {
        return result(script, sender, args, false).cbool
    }

    override fun evalScript(script: String, sender: CommandSender, args: HashMap<String, Any>) {
        runKether {
            KetherShell.eval(
                script, ScriptOptions.new {
                    sender(sender)
                    vars(args)
                    namespace(namespaces)
                }
            )
        }
    }

    override fun result(script: String, sender: CommandSender?, args: HashMap<String, Any>, isFunc: Boolean): Any? {
        return try {
            KetherShell.eval(
                script, ScriptOptions.new {
                    sender?.let { sender(it) }
                    vars(args)
                    namespace(namespaces)
                }
            ).get()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}