package me.mkbaka.executableblock.internal.trigger

import me.mkbaka.executableblock.api.block.EventAdapter
import me.mkbaka.executableblock.api.block.Trigger
import me.mkbaka.executableblock.internal.block.BlockManager
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import taboolib.library.reflex.Reflex.Companion.invokeMethod

abstract class BukkitTrigger : Trigger<Event> {

    override fun call(event: EventAdapter) {
        val loc = getBlock(event)?.location ?: return
        BlockManager.callExecute(loc, event as BukkitEventAdapter)
    }

    /**
     * 用于过滤触发
     * 防止某些误判
     */
    open fun condition(event: EventAdapter): Boolean {
        if (event !is BukkitEventAdapter || getPlayer(event) == null) return false
        val block = getBlock(event) ?: return false
        return block.type != Material.AIR
                && BlockManager.isExecutableBlock(block)
                && BlockManager.isSameTrigger(block, this)
    }

    /**
     * 获取事件涉及到的方块
     */
    open fun getBlock(event: EventAdapter): Block? {
        return (event as BukkitEventAdapter).event.invokeMethod<Block>("getBlock")
    }

    /**
     * 获取事件涉及到的玩家
     */
    open fun getPlayer(event: EventAdapter): Player? {
        return (event as BukkitEventAdapter).event.invokeMethod<Player>("getPlayer")
    }

    /**
     * 开摆!
     */
    override fun bindToEvent() {
        TriggerManager.bindToEvent(this)
    }

}