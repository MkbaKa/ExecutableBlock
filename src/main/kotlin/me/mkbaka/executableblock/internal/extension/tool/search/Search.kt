package me.mkbaka.executableblock.internal.extension.tool.search

import me.mkbaka.executableblock.ExecutableBlock.prefix
import me.mkbaka.executableblock.internal.block.BlockManager
import me.mkbaka.executableblock.internal.utils.Util.format
import org.bukkit.Location
import org.bukkit.command.CommandSender
import taboolib.platform.util.sendLang

object Search {

    /**
     * 获取附近已绑定的方块
     * @param [origin] 中心点
     * @param [distance] 距离
     * @param [execKey] execute节点
     * @return [List<Location>]
     */
    fun getNearBlock(origin: Location, distance: Double, execKey: String? = null): List<Location> {
        val locs = BlockManager.getBoundLocations()
        val box = BoundingBox.of(origin, distance, distance, distance)
        return if (!execKey.isNullOrEmpty()) {
            locs.filter { box.contains(it.x, it.y, it.z) && BlockManager.getBoundExecute(it) == execKey }
        } else {
            locs.filter { box.contains(it.x, it.y, it.z) }
        }
    }

    /**
     * 把坐标格式化发送给目标
     * @param [sender] 目标
     * @param [locs] 坐标
     */
    fun show(sender: CommandSender, locs: List<Location>) {
        sender.sendLang("command-near-header", prefix)
        locs.forEach {
            sender.sendLang("command-near-body", it.format(), BlockManager.getBoundExecute(it)!!, it.x, it.y, it.z)
        }
        sender.sendLang("command-near-footer")
    }

}