package me.mkbaka.executableblock.api

import me.mkbaka.executableblock.internal.block.BlockManager
import org.bukkit.Location
import org.bukkit.block.Block

object BlockAPI {

    /**
     * 绑定到指定方块的坐标
     * @param [exec] execute节点
     * @param [block] 方块
     * @return [Boolean]
     */
    fun bindTo(exec: String, block: Block): Boolean {
        return bindTo(exec, block.location)
    }

    /**
     * 绑定到指定坐标
     * @param [exec] execute节点
     * @param [block] 方块
     * @return [Boolean]
     */
    fun bindTo(exec: String, loc: Location): Boolean {
        return BlockManager.bindToLocation(exec, loc)
    }

    /**
     * 解绑
     * @param [block] 方块
     */
    fun unBind(block: Block) {
        unBind(block.location)
    }

    /**
     * 解绑
     * @param [loc] 坐标
     */
    fun unBind(loc: Location) {
        BlockManager.unBind(loc)
    }

    /**
     * 该方块的坐标是否被绑定
     * @param [block] 方块
     */
    fun isBound(block: Block) =
        BlockManager.isExecutableBlock(block)

    /**
     * 该坐标是否被绑定
     * @param [loc] 坐标
     */
    fun isBound(loc: Location) =
        BlockManager.isExecutableBlock(loc)

}