package me.mkbaka.executableblock.internal.hook.region

import me.mkbaka.executableblock.api.event.RegionEvent
import me.mkbaka.executableblock.internal.utils.Util.notNullIgnoreEquals
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.releaseResourceFile

/**
 * 区域管理
 *
 * @author Mkbakaa
 * @date 2023/05/03
 */
object RegionManager {

    /**
     * 玩家是否在区域内
     *
     * @param [player] 玩家
     * @return [Boolean]
     */
    fun inRegion(player: Player): Boolean {
        return inRegion(player.location)
    }

    /**
     * 玩家是否在指定区域内
     * @param [player] 玩家
     * @param [region] 区域名
     * @return [Boolean]
     */
    fun inRegion(player: Player, region: String): Boolean {
        return inRegion(player.location, region)
    }

    /**
     * 坐标是否在某个区域内
     * @param [loc] 坐标
     * @return [Boolean]
     */
    fun inRegion(loc: Location): Boolean {
        return RegionHook.inst?.inRegion(loc) ?: error("未找到已支持的区域插件, 请检查插件列表 或删掉相关配置.")
    }

    /**
     * 坐标是否在指定区域内
     * @param [loc] 坐标
     * @param [region] 区域名
     * @return [Boolean]
     */
    fun inRegion(loc: Location, region: String): Boolean {
        return if (region.isEmpty()) inRegion(loc) else RegionHook.inst?.inRegion(loc, region) ?: error("未找到已支持的区域插件, 请检查插件列表 或删掉相关配置.")
    }

    /**
     * 获取 Location 所在的区域 (可为空)
     * @param [loc] 坐标
     * @return [Region?]
     */
    fun getRegionAt(loc: Location): Region? {
        return RegionHook.inst?.getRegionAt(loc)
    }

    /**
     * 获取 Location 所在的所有区域 (可为空列表)
     * @param [loc] 坐标
     * @return [Collection<Region>]
     */
    fun getRegionsAt(loc: Location): Collection<Region> {
        return RegionHook.inst?.getRegionsAt(loc) ?: error("未找到已支持的区域插件, 请检查插件列表 或删掉相关配置.")
    }

    /**
     ***** 可能会导致占用过高...
     *
     * 注册区域事件管理
     */
    @Awake(LifeCycle.ACTIVE)
    fun reg() {
        if (RegionHook.inst == null) return
        releaseResourceFile("global/AntiMove.yml", replace = false)

        registerBukkitListener(
            PlayerMoveEvent::class.java, EventPriority.HIGHEST, false
        ) {
            if (it.to == null || it.to == it.from || it.to.notNullIgnoreEquals(it.from)) return@registerBukkitListener
            val fromRegions = getRegionsAt(it.from)
            val toRegions = getRegionsAt(it.to!!)

            fromRegions.forEach { region ->
                if (!toRegions.contains(region)) {
                    val event = RegionEvent.Exit(it.player, region).apply { call() }
                    if (event.isCancelled) it.isCancelled = true
                }
            }

            toRegions.forEach { region ->
                if (!fromRegions.contains(region)) {
                    val event = RegionEvent.Enter(it.player, region).apply { call() }
                    if (event.isCancelled) it.isCancelled = true
                }
            }

        }
    }

}