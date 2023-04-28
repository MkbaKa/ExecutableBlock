package me.mkbaka.executableblock.internal.hook

import me.mkbaka.executableblock.internal.extension.cooldown.CooldownManager
import org.bukkit.entity.Player
import taboolib.platform.compat.PlaceholderExpansion

object PlaceholderAPIHook : PlaceholderExpansion {

    override val identifier: String
        get() = "execb"

    override fun onPlaceholderRequest(player: Player?, args: String): String {
        player!!
        val splits = args.split("_")
        return when (splits[0].lowercase()) {
            "cool" -> {
                /**
                 * %execb_cool_get_key%
                 */
                when (splits.getOrElse(1) { "get" }.lowercase()) {
                    "get" -> CooldownManager.getTime(player.uniqueId, splits.getOrElse(2) { error("Unknow source key") })
                    else -> ""
                }
            }
            else -> ""
        }.toString()
    }

}