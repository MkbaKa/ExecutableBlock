package me.mkbaka.executableblock.api

import me.mkbaka.executableblock.internal.block.BlockManager
import org.bukkit.Location
import org.bukkit.block.Block

object BlockAPI {

    fun bindTo(exec: String, block: Block): Boolean {
        return bindTo(exec, block.location)
    }

    fun bindTo(exec: String, loc: Location): Boolean {
        return BlockManager.bindToLocation(exec, loc)
    }

    fun unBind(block: Block) {
        unBind(block.location)
    }

    fun unBind(loc: Location) {
        BlockManager.unBind(loc)
    }
    
    fun isBound(block: Block) =
        BlockManager.isExecutableBlock(block)
    
    fun isBound(loc: Location) =
        BlockManager.isExecutableBlock(loc)

}