package me.mkbaka.executableblock.internal.block

import me.mkbaka.executableblock.internal.executes.ExecuteManager
import me.mkbaka.executableblock.internal.trigger.BukkitEventAdapter
import me.mkbaka.executableblock.internal.trigger.BukkitTrigger
import me.mkbaka.executableblock.internal.trigger.TriggerManager
import org.bukkit.entity.Player
import taboolib.library.configuration.ConfigurationSection

class ExecutableBlock(root: ConfigurationSection) {

    val trigger = (TriggerManager.keyToTrigger(root.getString("trigger")!!.lowercase())) as BukkitTrigger
    private val executes = root.getMapList("executes").map {
        Action(it)
    }

    fun run(event: BukkitEventAdapter) {
        val player = trigger.getPlayer(event)!!
        executes.forEach {
            if (it.checkCondition(event, player)) {
                it.evalAction(event, player)
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

        fun checkCondition(event: BukkitEventAdapter, player: Player): Boolean {
            if (map.keys.first() == "else") return true
            val condition = map["condition"].toString()
            return ExecuteManager.execute(condition, player, hashMapOf("event" to event.event))
        }

        fun evalAction(event: BukkitEventAdapter, player: Player) {
            ExecuteManager.execute(action, player, hashMapOf("event" to event.event))
        }

    }

}