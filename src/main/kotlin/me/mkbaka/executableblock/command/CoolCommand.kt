package me.mkbaka.executableblock.command

import me.mkbaka.executableblock.ExecutableBlock.prefix
import me.mkbaka.executableblock.internal.extension.cooldown.CooldownManager
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggestPlayers
import taboolib.common5.clong
import taboolib.module.lang.sendLang
import java.util.*
import java.util.concurrent.TimeUnit

object CoolCommand {

    @CommandBody
    val test = subCommand {
        execute<ProxyPlayer> { sender, context, argument ->
            editCool(EditType.ADD, sender.uniqueId, "test", 10000)
        }
    }

    @CommandBody
    val add = subCommand {
        dynamic("player") {
            suggestPlayers()

            dynamic("source") {
                suggestionUncheck<ProxyCommandSender> { sender, context ->
                    listOf("asdasdasdasd")
                }

                dynamic("time") {
                    suggestionUncheck<ProxyCommandSender> { sender, context ->
                        listOf("1000")
                    }

                    execute<ProxyCommandSender> { sender, context, argument ->
                        val player = Bukkit.getPlayerExact(context["player"])
                            ?: return@execute sender.sendLang("command-invalid-player", prefix, context["player"])
                        editCool(EditType.ADD, player.uniqueId, context["source"], context["time"].clong)
                    }

                    dynamic("timeunit") {
                        suggestion<ProxyCommandSender> { sender, context ->
                            TimeUnit.values().map { it.name }
                        }

                        execute<ProxyCommandSender> { sender, context, argument ->
                            val player = Bukkit.getPlayerExact(context["player"])
                                ?: return@execute sender.sendLang("command-invalid-player", prefix, context["player"])
                            editCool(EditType.TAKE, player.uniqueId, context["source"], context["time"].clong, TimeUnit.valueOf(context["timeunit"]))
                        }
                    }

                }
            }
        }
    }

    @CommandBody
    val take = subCommand {
        dynamic("player") {
            suggestPlayers()

            dynamic("source") {
                suggestionUncheck<ProxyCommandSender> { sender, context ->
                    listOf("asdasdasdasd")
                }

                dynamic("time") {
                    suggestionUncheck<ProxyCommandSender> { sender, context ->
                        listOf("1000")
                    }

                    execute<ProxyCommandSender> { sender, context, argument ->
                        val player = Bukkit.getPlayerExact(context["player"])
                            ?: return@execute sender.sendLang("command-invalid-player", prefix, context["player"])
                        editCool(EditType.TAKE, player.uniqueId, context["source"], context["time"].clong)
                    }

                    dynamic("timeunit") {
                        suggestion<ProxyCommandSender> { sender, context ->
                            TimeUnit.values().map { it.name }
                        }

                        execute<ProxyCommandSender> { sender, context, argument ->
                            val player = Bukkit.getPlayerExact(context["player"])
                                ?: return@execute sender.sendLang("command-invalid-player", prefix, context["player"])
                            editCool(EditType.TAKE, player.uniqueId, context["source"], context["time"].clong, TimeUnit.valueOf(context["timeunit"]))
                        }
                    }

                }
            }
        }
    }

    fun editCool(type: EditType, uuid: UUID, source: String, time: Long = 0, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Long {
        return when (type) {
            EditType.ADD -> {
                CooldownManager.addCooling(uuid, source, time, timeUnit)
                -1
            }
            EditType.TAKE -> {
                CooldownManager.takeCooling(uuid, source, time, timeUnit)
                -1
            }
            EditType.GET -> CooldownManager.getTime(uuid, source)
        }
    }

    enum class EditType {
        ADD, TAKE, GET
    }
}