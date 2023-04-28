package me.mkbaka.executableblock.internal.storage

import me.mkbaka.executableblock.internal.block.BlockManager
import me.mkbaka.executableblock.internal.extension.cooldown.Cooldown
import me.mkbaka.executableblock.internal.storage.impl.JsonBlockStorage
import me.mkbaka.executableblock.internal.settings.Settings
import me.mkbaka.executableblock.internal.settings.Settings.settings
import org.bukkit.Bukkit
import org.bukkit.Location
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor
import taboolib.common5.cdouble
import taboolib.common5.cfloat
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * 该部分代码极其拉稀
 ***请自行备好降压药
 */
abstract class Storage {

    protected val dataCache = ConcurrentHashMap<String, HashSet<Location>>()

    protected var updateTask: PlatformExecutor.PlatformTask? = null

    open fun format(loc: Location): String {
        return "${loc.world!!.name}, ${loc.blockX}, ${loc.blockY}, ${loc.blockZ}, ${loc.pitch}, ${loc.yaw}"
    }

    open fun deFormat(str: String): Location {
        val splits = str.split(", ")
        return Location(
            Bukkit.getWorld(splits[0]),
            splits[1].cdouble,
            splits[2].cdouble,
            splits[3].cdouble,
            splits[4].cfloat,
            splits[5].cfloat
        )
    }

    fun updateToCache(node: String, loc: Location) {
        dataCache.getOrPut(node) { hashSetOf() }.add(loc)
    }

    fun removeFromCache(node: String, loc: Location) {
        if (dataCache.containsKey(node)) dataCache[node]!!.remove(loc)
    }

    fun getDataCache() = hashMapOf<String, HashSet<Location>>().apply { putAll(dataCache) }

    abstract fun save(node: String, loc: Location)

    abstract fun saveAll()

    abstract fun loadAll()

    abstract fun saveCool(uuid: UUID, cooldown: Cooldown)

    abstract fun loadAllCool()

    companion object {

        val inst by lazy {
            val type = settings.getString("Storage.type", "json")!!
            when (type.lowercase()) {
                "json" -> JsonBlockStorage
                else -> error("未支持的存储类型 $type")
            }
        }

        @Awake(LifeCycle.ACTIVE)
        fun active() {
            inst.loadAll()
            inst.loadAllCool()
            BlockManager.load(inst.getDataCache())
            inst.apply {
                updateTask = submitAsync(period = Settings.updatePeriod) {
                    saveAll()
                }
            }
        }

        @Awake(LifeCycle.DISABLE)
        fun disable() {
            inst.apply {
                saveAll()
                updateTask?.cancel()
            }
        }

    }

}