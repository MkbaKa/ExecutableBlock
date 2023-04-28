package me.mkbaka.executableblock.command

import me.mkbaka.executableblock.ExecutableBlock.prefix
import me.mkbaka.executableblock.internal.extension.tool.copy.CopyWand
import me.mkbaka.executableblock.internal.extension.tool.search.Search
import me.mkbaka.executableblock.internal.settings.Settings
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggestPlayers
import taboolib.common5.cdouble
import taboolib.module.lang.sendLang

object ExtenCommand {

    @CommandBody
    val copywand = subCommand {
        dynamic("player") {
            suggestPlayers()

            dynamic("execute") {
                suggestion<ProxyCommandSender> { sender, context ->
                    Settings.executes.keys.toList()
                }

                execute<ProxyCommandSender> { sender, context, argument ->
                    val player = Bukkit.getPlayerExact(context["player"]) ?: return@execute sender.sendLang(
                        "command-invalid-player",
                        prefix, context["player"]
                    )
                    CopyWand.giveCopyWand(player, context["execute"])
                    sender.sendLang("command-tool-wand", prefix, context["player"])
                }
            }
        }
    }

    @CommandBody
    val near = subCommand {
        dynamic("player") {
            suggestPlayers()

            dynamic("radius") {
                suggestionUncheck<ProxyCommandSender> { sender, context ->
                    listOf("5")
                }

                execute<ProxyCommandSender> { sender, context, argument ->
                    val player = Bukkit.getPlayerExact(context["player"]) ?: return@execute sender.sendLang(
                        "command-invalid-player",
                        prefix, context["player"]
                    )
                    showTo(player, context["radius"].cdouble)
                }

                dynamic("execute") {
                    suggestion<ProxyCommandSender> { sender, context ->
                        Settings.executes.keys.toList()
                    }

                    execute<ProxyCommandSender> { sender, context, argument ->
                        val player = Bukkit.getPlayerExact(context["player"]) ?: return@execute sender.sendLang(
                            "command-invalid-player",
                            prefix, context["player"]
                        )
                        showTo(player, context["radius"].cdouble, context["execute"])
                    }
                }
            }
        }
    }

    private fun showTo(player: Player, radius: Double, exec: String? = null) {
        Search.show(player, Search.getNearBlock(player.location, radius, exec))
    }
}