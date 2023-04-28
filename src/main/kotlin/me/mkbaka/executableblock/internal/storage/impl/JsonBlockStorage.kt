package me.mkbaka.executableblock.internal.storage.impl

import me.mkbaka.executableblock.internal.extension.cooldown.Cooldown
import me.mkbaka.executableblock.internal.extension.cooldown.CooldownManager
import me.mkbaka.executableblock.internal.storage.FileStorage
import me.mkbaka.executableblock.internal.utils.FileUtil.executeSubFiles
import org.bukkit.Location
import taboolib.common.io.newFile
import taboolib.common.platform.function.getDataFolder
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.module.configuration.util.asMap
import java.util.*

object JsonBlockStorage : FileStorage() {

    override val folder by lazy {
        newFile(getDataFolder(), "storage", create = true, folder = true)
    }

    override fun save(node: String, loc: Location) {
        val file = node.toStorageFile

        val json = node.toConfiguration
        val locs = if (json.contains("locations")) json.getStringList("locations") else emptyList()
        val format = format(loc)
        if (!locs.contains(format)) {
            json["locations"] = locs.plus(format)
            json.saveToFile(file)
        }
    }

    override fun saveAll() {
        dataCache.forEach { (node, locs) ->
            val file = node.toStorageFile

            val json = node.toConfiguration
            json["locations"] = locs.map(::format).toList()
            json.saveToFile(file)
        }
    }

    override fun loadAll() {
        folder.executeSubFiles {
            if (it.extension != "json") return@executeSubFiles
            val json = Configuration.loadFromFile(it, Type.JSON)
            if (!json.contains("locations")) return@executeSubFiles
            json.getStringList("locations").forEach { loc ->
                dataCache.getOrPut(it.nameWithoutExtension) { hashSetOf() }.add(deFormat(loc))
            }
        }
    }

    override fun saveCool(uuid: UUID, cooldown: Cooldown) {
        cooldown.getCoolCache().forEach { (node, cooling) ->
            val file = node.toStorageFile
            val json = node.toConfiguration

            val cooldowns = (if (json.contains("cooldowns")) json["cooldowns"]!!.asMap() else emptyMap()).toMutableMap()

            if (!cooling.isTimeout) {
                cooldowns[uuid.toString()] = cooling.totalTime
            } else {
                if (cooldowns.contains(uuid.toString())) {
                    cooldowns.remove(uuid.toString())
                }
            }

            json["cooldowns"] = cooldowns
            json.saveToFile(file)
        }
    }

    override fun loadAllCool() {
        folder.executeSubFiles {
            if (it.extension != "json") return@executeSubFiles
            val json = Configuration.loadFromFile(it, Type.JSON)
            if (!json.contains("cooldowns")) return@executeSubFiles
            json.getConfigurationSection("cooldowns")!!.apply {
                getKeys(false).forEach { uuid ->
                    CooldownManager.setCooling(UUID.fromString(uuid), it.nameWithoutExtension, getLong(uuid))
                }
            }
        }
    }

}