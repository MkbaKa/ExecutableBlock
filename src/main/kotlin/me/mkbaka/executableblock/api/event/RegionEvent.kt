package me.mkbaka.executableblock.api.event

import me.mkbaka.executableblock.internal.hook.region.Region
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * 区域事件
 * @author Mkbakaa
 * @date 2023/05/03
 */
class RegionEvent {

    /**
     * 进入区域
     * @param [player] 玩家
     * @param [region] 区域
     */
    class Enter(val player: Player, val region: Region): BukkitProxyEvent()

    /**
     * 退出区域
     * @param [player] 玩家
     * @param [region] 区域
     */
    class Exit(val player: Player, val region: Region): BukkitProxyEvent()

}