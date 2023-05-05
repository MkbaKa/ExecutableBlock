package me.mkbaka.executableblock.internal.trigger.impl

import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.trigger.BukkitEventAdapter
import me.mkbaka.executableblock.internal.trigger.BukkitTrigger
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

@AutoRegister("left click")
object PlayerLeftClickTrigger : BukkitTrigger() {

    override val eventClass: Class<out Event>
        get() = PlayerInteractEvent::class.java

    override fun getBlock(event: BukkitEventAdapter): Block? {
        return (event.event as? PlayerInteractEvent)?.clickedBlock
    }

    override fun getPlayer(event: BukkitEventAdapter): Player? {
        return (event.event as? PlayerEvent)?.player
    }

    override fun condition(event: BukkitEventAdapter): Boolean {
        val e = event.event as? PlayerInteractEvent ?: return false
        return e.hand == EquipmentSlot.HAND && e.action == Action.LEFT_CLICK_BLOCK && super.condition(event)
    }

}