package me.mkbaka.executableblock.internal.extension.cooldown

import me.mkbaka.executableblock.internal.storage.Storage.Companion.inst
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object CooldownManager {

    private val coolDatas = ConcurrentHashMap<UUID, Cooldown>()

    /**
     * 给js用的
     */
    fun addCooling(uuid: UUID, source: String, time: Long) {
        addCooling(uuid, source, time, TimeUnit.MILLISECONDS)
    }

    fun takeCooling(uuid: UUID, source: String, time: Long) {
        takeCooling(uuid, source, time, TimeUnit.MILLISECONDS)
    }

    fun setCooling(uuid: UUID, source: String, time: Long) {
        setCooling(uuid, source, time, TimeUnit.MILLISECONDS)
    }

    /**
     * 开发建议使用这些
     */
    fun addCooling(uuid: UUID, source: String, time: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) {
        coolDatas.getOrPut(uuid) { Cooldown() }.addCooling(source, time, timeUnit)
    }

    fun takeCooling(uuid: UUID, source: String, time: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) {
        coolDatas.getOrPut(uuid) { Cooldown() }.takeCooling(source, time, timeUnit)
    }

    fun setCooling(uuid: UUID, source: String, time: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) {
        coolDatas.getOrPut(uuid) { Cooldown() }.setCooling(source, time, timeUnit)
    }

    fun isTimeout(uuid: UUID, source: String): Boolean {
        return coolDatas[uuid]?.isTimeout(source) ?: true
    }

    fun getTime(uuid: UUID, source: String): Long {
        return coolDatas[uuid]?.getTime(source) ?: 0
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        coolDatas.forEach {
            inst.saveCool(it.key, it.value)
        }
    }

}