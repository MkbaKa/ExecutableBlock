package me.mkbaka.executableblock.internal.hook.region

import me.mkbaka.executableblock.internal.hook.region.impl.Residence
import me.mkbaka.executableblock.internal.hook.region.impl.WorldGuard
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

interface RegionHook {

    fun inRegion(player: Player): Boolean

    fun inRegion(player: Player, region: String): Boolean

    fun inRegion(loc: Location): Boolean

    fun inRegion(loc: Location, region: String): Boolean

    fun getRegionAt(loc: Location): Region?

    fun getRegionsAt(loc: Location): Collection<Region>

    companion object {

        private val supports = hashMapOf(
            "Residence" to Residence,
            "WorldGuard" to WorldGuard
        )

        val inst: RegionHook? by lazy {
            supports.forEach {
                if (Bukkit.getPluginManager().isPluginEnabled(it.key)) {
                    return@lazy it.value
                }
            }
            null
        }

    }

}

interface Region