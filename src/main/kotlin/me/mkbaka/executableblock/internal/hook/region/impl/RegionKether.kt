package me.mkbaka.executableblock.internal.hook.region.impl

import me.mkbaka.executableblock.internal.hook.region.RegionManager
import me.mkbaka.executableblock.internal.utils.nextArgumentAction
import org.bukkit.entity.Player
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture

class RegionKether {

    class InRegion(val region: ParsedAction<*>) : ScriptAction<Boolean>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Boolean> {
            val completableFuture = CompletableFuture<Boolean>()
            frame.newFrame(region).run<String>().thenApply { region ->
                completableFuture.complete(RegionManager.inRegion(frame.player().cast<Player>(), region))
            }
            return completableFuture
        }
    }

    companion object {

        /**
         * inRegion
         * inRegion region [name]
         */
        @KetherParser(["inRegion", "inregion"], namespace = "ExecutableBlock", shared = true)
        fun parser() = scriptParser {
            InRegion(it.nextArgumentAction(arrayOf("region", "r"),"")!!)
        }

    }

}