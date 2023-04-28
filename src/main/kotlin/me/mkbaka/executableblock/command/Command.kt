package me.mkbaka.executableblock.command

import me.mkbaka.executableblock.ExecutableBlock.prefix
import me.mkbaka.executableblock.internal.settings.Settings
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.module.lang.sendLang

@CommandHeader("eb", aliases = ["executableblock", "exeb"], permissionDefault = PermissionDefault.OP)
object Command {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val block = BlockCommand

    @CommandBody
    val cooldown = CoolCommand

    @CommandBody
    val exten = ExtenCommand

    @CommandBody
    val reload = subCommand {
        execute<ProxyCommandSender> { sender, context, argument ->
            Settings.reload()
            sender.sendLang("command-reload", prefix)
        }
    }

}