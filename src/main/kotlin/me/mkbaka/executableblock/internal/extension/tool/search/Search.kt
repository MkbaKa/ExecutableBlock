package me.mkbaka.executableblock.internal.extension.tool.search

import me.mkbaka.executableblock.ExecutableBlock.prefix
import me.mkbaka.executableblock.internal.block.BlockManager
import me.mkbaka.executableblock.internal.utils.Util.format
import org.bukkit.Location
import org.bukkit.command.CommandSender
import taboolib.platform.util.sendLang

object Search {

    fun getNearBlock(origin: Location, distance: Double, execKey: String? = null): List<Location> {
        val locs = BlockManager.getBoundLocations()
        val box = BoundingBox.of(origin, distance, distance, distance)
        return if (!execKey.isNullOrEmpty()) {
            locs.filter { box.contains(it.x, it.y, it.z) && BlockManager.getBoundExecute(it) == execKey }
        } else {
            locs.filter { box.contains(it.x, it.y, it.z) }
        }
    }

    fun show(sender: CommandSender, locs: List<Location>) {
        sender.sendLang("command-near-header", prefix)
        locs.forEach {
            sender.sendLang("command-near-body", it.format(), BlockManager.getBoundExecute(it)!!, it.x, it.y, it.z)
        }
        sender.sendLang("command-near-footer")
    }

}