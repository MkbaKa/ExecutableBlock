package me.mkbaka.executableblock.internal.hook.region.impl

import com.sk89q.worldedit.Vector
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import me.mkbaka.executableblock.internal.hook.region.Region
import me.mkbaka.executableblock.internal.hook.region.RegionHook
import org.bukkit.Location
import org.bukkit.entity.Player

object WorldGuard : RegionHook {

    private val inst by lazy {
        WorldGuardPlugin.inst()
    }

    override fun inRegion(player: Player): Boolean {
        return inRegion(player.location)
    }

    override fun inRegion(player: Player, region: String): Boolean {
        return inRegion(player.location, region)
    }

    override fun inRegion(loc: Location): Boolean {
        return getRegionAt(loc) != null
    }

    override fun inRegion(loc: Location, region: String): Boolean {
        val reg = inst.regionContainer.get(loc.world)?.getRegion(region) ?: return false
        return reg.contains(loc.toVec())
    }

    override fun getRegionAt(loc: Location): Region? {
        val manager = inst.getRegionManager(loc.world) ?: return null
        val vec = loc.toVec()
        return manager.regions.entries.first { it.value.contains(vec) }.value?.let { WorldGuardRegion(it) }
    }

    override fun getRegionsAt(loc: Location): Collection<Region> {
        return inst.getRegionManager(loc.world)?.getApplicableRegions(loc)?.map { WorldGuardRegion(it) } ?: emptySet()
    }

    private fun Location.toVec(): Vector {
        return Vector(x, y, z)
    }

}

class WorldGuardRegion(val region: ProtectedRegion) : Region