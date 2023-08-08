package me.mkbaka.executableblock.internal.executes

import org.bukkit.command.CommandSender

interface Execute {

    fun eval(script: String, sender: CommandSender, args: HashMap<String, Any>): Boolean

    fun evalScript(script: String, sender: CommandSender, args: HashMap<String, Any>)

    fun result(
        script: String,
        sender: CommandSender? = null,
        args: HashMap<String, Any> = hashMapOf(),
        isFunc: Boolean = true
    ): Any?

}