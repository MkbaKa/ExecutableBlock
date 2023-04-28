package me.mkbaka.executableblock.internal.trigger

import me.mkbaka.executableblock.api.block.EventAdapter
import org.bukkit.event.Event

class BukkitEventAdapter(
    val event: Event
) : EventAdapter