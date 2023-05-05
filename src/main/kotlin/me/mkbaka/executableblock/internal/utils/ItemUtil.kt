package me.mkbaka.executableblock.internal.utils

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.module.nms.getName
import taboolib.platform.util.hasLore
import taboolib.platform.util.hasName

object ItemUtil {

    /**
     * 判断材质是否相同
     * @param [item] 物品A
     * @param [target] 物品B
     * @return [Boolean]
     */
    fun checkType(item: ItemStack, target: ItemStack): Boolean {
        return item.type == target.type
    }

    /**
     * 判断名字是否相同 (若没有名字会判断原版物品名)
     * @param [item] 物品A
     * @param [target] 物品B
     * @return [Boolean]
     */
    fun checkName(item: ItemStack, target: ItemStack): Boolean {
        return getName(item) == getName(target)
    }

    /**
     * 物品名内是否包含指定字符串 若字符串为空则判断是否有自定义物品名
     * @param [item] 物品
     * @param [name] 字符串
     * @return [Boolean]
     */
    fun hasName(item: ItemStack, name: String? = null): Boolean {
        return item.hasName(name)
    }

    /**
     * 物品lore内是否包含指定字符串(模糊匹配) 若字符串为空则判断是否有自定义lore
     * @param [item] 物品
     * @param [lore] 描述
     * @return [Boolean]
     */
    fun hsaLore(item: ItemStack, lore: String? = null): Boolean {
        return item.hasLore(lore)
    }

    /**
     * 物品是否有指定nbt  若value不为空则判断nbt值是否相同
     * @param [item] 物品
     * @param [key] 节点
     * @param [value] 值
     * @return [Boolean]
     */
    fun hasNBT(item: ItemStack, key: String, value: Any? = null): Boolean {
        val itemTag = item.getItemTag()
        val nbtData = if (key.contains(".")) itemTag.getDeep(key) else itemTag[key] ?: return false
        return if (value != null) nbtData.equals(ItemTagData.toNBT(value)) else nbtData != null
    }

    /**
     * 获取物品名 若没有物品名会根据玩家获取本地译名
     * @param [item] 物品
     * @param [player] 玩家
     * @return [String]
     */
    fun getName(item: ItemStack, player: Player? = null): String {
        return item.getName(player)
    }

    /**
     * 获取主手物品
     * @param [player] 玩家
     * @return [ItemStack]
     */
    fun getMainHandItem(player: Player): ItemStack {
        return player.inventory.itemInMainHand
    }

    /**
     * 获取副手物品
     * @param [player] 玩家
     * @return [ItemStack]
     */
    fun getOffHandItem(player: Player): ItemStack {
        return player.inventory.itemInOffHand
    }

}