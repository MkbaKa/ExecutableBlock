package me.mkbaka.executableblock.internal.hook.region.impl

import com.bekvon.bukkit.residence.Residence
import com.bekvon.bukkit.residence.protection.ClaimedResidence
import me.mkbaka.executableblock.internal.hook.region.Region
import me.mkbaka.executableblock.internal.hook.region.RegionHook
import org.bukkit.Location
import org.bukkit.entity.Player

object Residence : RegionHook {

    private val inst by lazy {
        Residence.getInstance()
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
        return inst.residenceManager.getByLoc(loc)?.residenceName == region
    }

    override fun getRegionAt(loc: Location): Region? {
        return inst.residenceManager.getByLoc(loc)?.let { ResidenceRegion(it) }
    }

    override fun getRegionsAt(loc: Location): Collection<Region> {
        return getRegionAt(loc)?.let { hashSetOf(it) } ?: emptySet()
    }

}

class ResidenceRegion(val region: ClaimedResidence) : Region