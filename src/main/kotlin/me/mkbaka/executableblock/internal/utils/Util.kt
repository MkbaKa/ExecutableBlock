package me.mkbaka.executableblock.internal.utils

import org.bukkit.Location
import org.bukkit.block.Block

object Util {

    fun Block.format(): String {
        return "${type.name}, ${world.name}, ${location.blockX}, ${location.blockY}, ${location.blockZ}"
    }

    fun Location.format(): String {
        return block.format()
    }

    fun Location?.notNullIgnoreEquals(target: Location) =
        this != null && ignoreEquals(target)

    fun Location.ignoreEquals(loc: Location) =
        this.x == loc.x && this.y == loc.y && this.z == loc.z

    fun Location?.notNullYawPitchCheck(target: Location) =
        this != null && yawPitchCheck(target)

    fun Location.yawPitchCheck(loc: Location) =
        this.yaw == loc.yaw && this.pitch == loc.pitch

}