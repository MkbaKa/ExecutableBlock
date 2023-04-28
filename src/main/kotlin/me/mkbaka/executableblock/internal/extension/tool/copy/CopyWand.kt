package me.mkbaka.executableblock.internal.extension.tool.copy

import me.mkbaka.executableblock.ExecutableBlock.prefix
import me.mkbaka.executableblock.api.BlockAPI
import me.mkbaka.executableblock.internal.block.BlockManager
import me.mkbaka.executableblock.internal.utils.Util.format
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.platform.util.*

object CopyWand {

    private fun isCopyWand(item: ItemStack) =
        !item.isAir && item.hasName() && item.hasLore() && item.getItemTag().containsKey("ExecuteableBlock")

    private fun getWandItem(exec: String): ItemStack {
        return buildItem(XMaterial.STICK) {
            name = "&e神奇魔杖"
            lore.addAll(
                listOf(
                    "&a左键 &f解除绑定被点击的方块.",
                    "&c右键 &f将 $exec &f绑定至被点击的方块."
                )
            )
            colored()
        }.apply {
            val itemTag = getItemTag()
            itemTag["ExecuteableBlock"] = ItemTagData.toNBT(hashMapOf("execute-type" to exec))
            itemTag.saveTo(this)
        }
    }

    fun giveCopyWand(player: Player, exec: String) {
        player.giveItem(getWandItem(exec))
    }

    @SubscribeEvent(EventPriority.HIGHEST, ignoreCancelled = false)
    fun rightClick(e: PlayerInteractEvent) {
        if (e.isCancelled || e.action != Action.RIGHT_CLICK_BLOCK || e.hand != EquipmentSlot.HAND
            || !e.player.isOp) return

        val item = e.player.inventory.itemInMainHand
        if (!isCopyWand(item)) return

        val block = e.clickedBlock ?: return
        if (BlockAPI.isBound(block)) {
            e.player.sendLang("command-block-bound", prefix, block.format(), BlockManager.getBoundExecute(block)!!)
            return
        }

        val itemTag = item.getItemTag()["ExecuteableBlock"]!!.asCompound()
        val exec = itemTag["execute-type"]!!.asString()

        BlockAPI.bindTo(exec, block.location)
        e.player.sendLang("command-block-bind-success", prefix, exec, block.format())
        e.isCancelled = true
    }

    @SubscribeEvent(EventPriority.HIGHEST, ignoreCancelled = true)
    fun leftClick(e: PlayerInteractEvent) {
        if (e.isCancelled || e.action != Action.LEFT_CLICK_BLOCK || e.hand != EquipmentSlot.HAND
            || !e.player.isOp) return

        val item = e.player.inventory.itemInMainHand
        if (!isCopyWand(item)) return

        val block = e.clickedBlock ?: return
        if (!BlockAPI.isBound(block)) {
            e.player.sendLang("command-block-notbound", prefix, block.format())
            return
        }
        e.player.sendLang("command-block-unbind-success", prefix, BlockManager.getBoundExecute(block)!!, block.format())
        BlockAPI.unBind(block.location)
        e.isCancelled = true
    }

    @SubscribeEvent(EventPriority.HIGHEST, ignoreCancelled = true)
    fun block(e: BlockBreakEvent) {
        if (e.isCancelled || !e.player.isOp) return

        val item = e.player.inventory.itemInMainHand
        if (!isCopyWand(item)) return

        e.isCancelled = true
    }
}

