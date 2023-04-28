package me.mkbaka.executableblock.internal.executes

import me.mkbaka.executableblock.api.block.Registerable
import me.mkbaka.executableblock.internal.executes.impl.KetherImpl
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass

object ExecuteManager : Registerable<Execute>() {

    override val root: KClass<Execute>
        get() = Execute::class

    fun execute(script: String, sender: CommandSender, args: HashMap<String, Any> = hashMapOf()): Boolean {
        registers.forEach { (prefix, exec) ->
            if (script.startsWith(prefix)) {
                return exec.eval(script.removePrefix("$prefix:"), sender, args)
            }
        }
        return KetherImpl.eval(script, sender, args)
    }

}