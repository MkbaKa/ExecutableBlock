package me.mkbaka.executableblock.internal.trigger.impl

import me.mkbaka.executableblock.api.block.EventAdapter
import me.mkbaka.executableblock.internal.autoregister.AutoRegister
import me.mkbaka.executableblock.internal.trigger.BukkitEventAdapter
import me.mkbaka.executableblock.internal.trigger.BukkitTrigger
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent

@AutoRegister("block break", alias = ["break block"])
object BlockBreakTrigger : BukkitTrigger() {

    override val eventClass: Class<out Event>
        get() = BlockBreakEvent::class.java

    override fun getBlock(event: EventAdapter): Block? {
        return ((event as BukkitEventAdapter).event as? BlockBreakEvent)?.block
    }

    override fun getPlayer(event: EventAdapter): Player {
        return ((event as BukkitEventAdapter).event as BlockBreakEvent).player
    }

}