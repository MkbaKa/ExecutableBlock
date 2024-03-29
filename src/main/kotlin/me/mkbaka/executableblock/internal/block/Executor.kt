package me.mkbaka.executableblock.internal.block

import me.mkbaka.executableblock.internal.executes.ExecuteManager
import me.mkbaka.executableblock.internal.trigger.BukkitEventAdapter
import me.mkbaka.executableblock.internal.trigger.BukkitTrigger
import me.mkbaka.executableblock.internal.utils.FileUtil.getTrigger
import org.bukkit.block.Block
import org.bukkit.entity.Player
import taboolib.library.configuration.ConfigurationSection

class Executor(val root: ConfigurationSection, trigger: BukkitTrigger? = null) {

    val trigger = trigger ?: root.getTrigger()
    private val executes = root.getMapList("executes").map { Action(it) }
    fun run(event: BukkitEventAdapter) {
        val player = trigger.getPlayer(event) ?: error("未获取到事件 ${event.event.eventName} 内的玩家!")
        val block = trigger.getBlock(event) ?: error("未获取到事件 ${event.event.eventName} 内的方块!")
        executes.forEach {
            if (it.checkCondition(event, player, block)) {
                it.evalAction(event, player, block)
                return
            }
        }
    }

    class Action(val map: Map<*, *>) {

        private val action by lazy {
            if (map.containsKey("else")) {
                (map["else"] as Map<String, String>)["action"]!!
            } else map["action"]!!.toString()
        }

        fun checkCondition(event: BukkitEventAdapter, player: Player, block: Block): Boolean {
            if (map.keys.first() == "else") return true
            return ExecuteManager.execute(
                map["condition"].toString(),
                player,
                hashMapOf("event" to event.event, "block" to block)
            )
        }

        fun evalAction(event: BukkitEventAdapter, player: Player, block: Block) {
            ExecuteManager.evalScript(action, player, hashMapOf("event" to event.event, "block" to block))
        }

    }

}