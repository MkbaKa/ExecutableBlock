package me.mkbaka.executableblock.internal.block

import me.mkbaka.executableblock.api.block.Trigger
import me.mkbaka.executableblock.internal.settings.SettingManager
import me.mkbaka.executableblock.internal.trigger.BukkitEventAdapter
import me.mkbaka.executableblock.internal.trigger.TriggerManager
import org.bukkit.Location
import org.bukkit.block.Block
import java.util.concurrent.ConcurrentHashMap
import me.mkbaka.executableblock.internal.storage.Storage.Companion.inst as storage

object BlockManager {

    private val locCaches = ConcurrentHashMap<Location, String>()

    /**
     * 运行触发器绑定的executor
     */
    fun callExecute(location: Location? = null, event: BukkitEventAdapter) {
        if (location == null || !locCaches.containsKey(location)) {
            val trigger = TriggerManager.eventToTrigger[event.event::class.java] ?: return
            SettingManager.getGlobalExecutors(trigger)?.forEach { (_, exec) ->
                exec.run(event)
            }
            return
        }
        val exec = getBoundExecute(location) ?: return
        SettingManager.getExecutor(exec)?.run(event)
    }

    /**
     * 一堆绑定操作
     */
    fun bindToLocations(node: String, locations: Collection<Location>): Boolean {
        locations.forEach {
            if (!bindToLocation(node, it)) return false
        }
        return true
    }

    fun bindToBlocks(node: String, blocks: Collection<Block>): Boolean {
        blocks.forEach {
            if (!bindToBlock(node, it)) return false
        }
        return true
    }

    fun bindToLocation(node: String, location: Location): Boolean {
        if (!SettingManager.hasExecutor(node)) return false
        locCaches[location] = node
        storage.updateToCache(node, location)
        return true
    }

    fun bindToBlock(node: String, block: Block): Boolean {
        return bindToLocation(node, block.location)
    }

    fun unBindLocations(locs: Collection<Location>) {
        locs.forEach(::unBind)
    }

    fun unBindBlocks(blocks: Collection<Block>) {
        blocks.forEach(::unBind)
    }

    fun unBind(loc: Location) {
        if (locCaches.containsKey(loc)) {
            storage.removeFromCache(getBoundExecute(loc)!!, loc)
            locCaches.remove(loc)
        }
    }

    fun unBind(block: Block) {
        unBind(block.location)
    }

    /**
     * 判断是否为绑定过的方块
     */
    fun isExecutableBlock(loc: Location): Boolean {
        return locCaches.any { it.key == loc }
    }

    fun isExecutableBlock(block: Block): Boolean {
        return isExecutableBlock(block.location)
    }

    /**
     * 防止某些傻逼情况出现
     * 比如 BlockBreakEvent 在破坏时会被 PlayerInteractEvent 监听到而报错
     */
    fun isSameTrigger(loc: Location, trigger: Trigger<*>): Boolean {
        if (!locCaches.containsKey(loc)) return false
        val exec = getBoundExecute(loc) ?: return false
        return SettingManager.getExecutor(exec)?.trigger?.equals(trigger) ?: false
    }

    fun isSameTrigger(block: Block, trigger: Trigger<*>): Boolean {
        return isSameTrigger(block.location, trigger)
    }

    /**
     * 获取坐标绑定的 Execute 节点
     */
    fun getBoundExecute(loc: Location): String? {
        return locCaches[loc]
    }

    fun getBoundExecute(block: Block): String? {
        return locCaches[block.location]
    }

    fun getBoundLocations(): List<Location> {
        return locCaches.keys().toList()
    }

    /**
     * 加载数据
     */
    fun load(map: Map<String, HashSet<Location>>) {
        map.forEach { (node, locs) ->
            locs.forEach {
                locCaches[it] = node
            }
        }
    }

}