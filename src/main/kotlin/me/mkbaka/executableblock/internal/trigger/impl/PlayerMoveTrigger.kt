package me.mkbaka.executableblock.internal.trigger.impl

import me.mkbaka.executableblock.api.block.EventAdapter
import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.trigger.BukkitEventAdapter
import me.mkbaka.executableblock.internal.trigger.BukkitTrigger
import me.mkbaka.executableblock.internal.utils.Util.notNullIgnoreEquals
import me.mkbaka.executableblock.internal.utils.Util.notNullYawPitchCheck
import me.mkbaka.executableblock.internal.utils.Util.yawPitchCheck
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerMoveEvent

@AutoRegister("move")
object PlayerMoveTrigger : BukkitTrigger() {

    override val eventClass: Class<out Event>
        get() = PlayerMoveEvent::class.java

    override fun getBlock(event: EventAdapter): Block? {
        if (event !is BukkitEventAdapter) return null
        return (event.event as? PlayerMoveEvent)?.to?.clone()?.subtract(0.0, 0.5, 0.0)?.block
    }

    override fun getPlayer(event: EventAdapter): Player? {
        return ((event as BukkitEventAdapter).event as? PlayerEvent)?.player
    }

    /**
     * 真傻逼啊这个事件
     */
    override fun condition(event: EventAdapter): Boolean {
        if (event !is BukkitEventAdapter) return false
        val e = (event.event as PlayerMoveEvent)
        return !e.to.notNullIgnoreEquals(e.from) && super.condition(event)
    }

}