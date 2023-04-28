package me.mkbaka.executableblock.internal.trigger.impl

import me.mkbaka.executableblock.api.block.EventAdapter
import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.trigger.BukkitEventAdapter
import me.mkbaka.executableblock.internal.trigger.BukkitTrigger
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

@AutoRegister("right click")
object PlayerRightClickTrigger : BukkitTrigger() {

    override val eventClass: Class<out Event>
        get() = PlayerInteractEvent::class.java

    override fun getBlock(event: EventAdapter): Block? {
        return ((event as BukkitEventAdapter).event as? PlayerInteractEvent)?.clickedBlock
    }

    override fun getPlayer(event: EventAdapter): Player? {
        return ((event as BukkitEventAdapter).event as? PlayerEvent)?.player
    }

    override fun condition(event: EventAdapter): Boolean {
        val e = (event as BukkitEventAdapter).event as? PlayerInteractEvent ?: return false
        return e.action == Action.RIGHT_CLICK_BLOCK && e.hand == EquipmentSlot.HAND && super.condition(event)
    }

}