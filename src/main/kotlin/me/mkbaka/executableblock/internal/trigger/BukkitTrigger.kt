package me.mkbaka.executableblock.internal.trigger

import me.mkbaka.executableblock.api.block.Trigger
import me.mkbaka.executableblock.internal.block.BlockManager
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import taboolib.library.reflex.Reflex.Companion.invokeMethod

abstract class BukkitTrigger : Trigger<BukkitEventAdapter> {

    abstract override val eventClass: Class<out Event>
    override fun call(event: BukkitEventAdapter): Boolean {
        val loc = getBlock(event)?.location ?: return false
        BlockManager.callExecute(loc, event)
        return true
    }

    override fun callGlobal(event: BukkitEventAdapter): Boolean {
        BlockManager.callExecute(null, event)
        return false
    }

    /**
     * 用于过滤触发
     * 防止某些误判
     */
    open fun condition(event: BukkitEventAdapter): Boolean {
        if (getPlayer(event) == null) return false
        val block = getBlock(event) ?: return false
        if (block.type == Material.AIR) return false
        if (!BlockManager.isExecutableBlock(block)) return callGlobal(event)
        return BlockManager.isSameTrigger(block, this)
    }

    /**
     * 获取事件涉及到的方块
     */
    open fun getBlock(event: BukkitEventAdapter): Block? {
        return event.event.invokeMethod<Block>("getBlock")
    }

    /**
     * 获取事件涉及到的玩家
     */
    open fun getPlayer(event: BukkitEventAdapter): Player? {
        return event.event.invokeMethod<Player>("getPlayer")
    }

    /**
     * 开摆!
     */
    override fun bindToEvent() {
        TriggerManager.bindToEvent(this)
    }

}