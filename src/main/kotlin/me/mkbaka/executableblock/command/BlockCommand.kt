package me.mkbaka.executableblock.command

import me.mkbaka.executableblock.ExecutableBlock.prefix
import me.mkbaka.executableblock.internal.block.BlockManager
import me.mkbaka.executableblock.internal.settings.Settings
import me.mkbaka.executableblock.internal.utils.Util.format
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang

object BlockCommand {

    @CommandBody
    val bind = subCommand {
        dynamic("execute") {
            suggestion<ProxyPlayer> { sender, context ->
                Settings.executes.keys.toList()
            }

            execute<ProxyPlayer> { sender, context, argument ->
                val block = sender.getTargetBlock()
                if (block.type == Material.AIR) return@execute sender.sendLang("command-block-add", prefix)
                if (BlockManager.isExecutableBlock(block)) return@execute sender.sendLang(
                    "command-block-bound",
                    prefix,
                    block.format(),
                    BlockManager.getBoundExecute(block)!!
                )

                BlockManager.bindToBlock(context["execute"], block)
                sender.sendLang("command-block-bind-success", prefix, context["execute"], block.format())
            }
        }
    }

    @CommandBody
    val unbind = subCommand {
        execute<ProxyPlayer> { sender, context, argument ->
            val block = sender.getTargetBlock()
            if (block.type == Material.AIR) return@execute sender.sendLang("command-block-add", prefix)
            if (!BlockManager.isExecutableBlock(block)) return@execute sender.sendLang(
                "command-block-notbound",
                prefix,
                block.format()
            )
            sender.sendLang("command-block-unbind-success", prefix, BlockManager.getBoundExecute(block)!!, block.format())
            BlockManager.unBind(block)
        }
    }

    private fun ProxyPlayer.getTargetBlock(distance: Int = 3): Block {
        return this.cast<Player>().getTargetBlock(null, distance)
    }

}