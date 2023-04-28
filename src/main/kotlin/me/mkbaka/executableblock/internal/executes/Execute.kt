package me.mkbaka.executableblock.internal.executes

import org.bukkit.command.CommandSender

abstract class Execute {

    abstract fun eval(script: String, sender: CommandSender, args: HashMap<String, Any>): Boolean

}