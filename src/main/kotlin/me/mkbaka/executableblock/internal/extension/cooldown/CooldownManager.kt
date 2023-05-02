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
     * 添加冷却
     * @param [uuid] uuid
     * @param [source] 源
     * @param [time] 时间
     */
    fun addCooling(uuid: UUID, source: String, time: Long) {
        addCooling(uuid, source, time, TimeUnit.MILLISECONDS)
    }

    /**
     * 减少冷却
     * @param [uuid] uuid
     * @param [source] 源
     * @param [time] 时间
     */
    fun takeCooling(uuid: UUID, source: String, time: Long) {
        takeCooling(uuid, source, time, TimeUnit.MILLISECONDS)
    }

    /**
     * 设置冷却
     * @param [uuid] uuid
     * @param [source] 源
     * @param [time] 时间
     */
    fun setCooling(uuid: UUID, source: String, time: Long) {
        setCooling(uuid, source, time, TimeUnit.MILLISECONDS)
    }

    /**
     * 开发建议使用这些
     * 添加冷却
     * @param [uuid] uuid
     * @param [source] 源
     * @param [time] 时间
     * @param [timeUnit] 时间单位
     */
    fun addCooling(uuid: UUID, source: String, time: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) {
        coolDatas.getOrPut(uuid) { Cooldown() }.addCooling(source, time, timeUnit)
    }

    /**
     * 减少冷却
     * @param [uuid] uuid
     * @param [source] 源
     * @param [time] 时间
     * @param [timeUnit] 时间单位
     */
    fun takeCooling(uuid: UUID, source: String, time: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) {
        coolDatas.getOrPut(uuid) { Cooldown() }.takeCooling(source, time, timeUnit)
    }

    /**
     * 设置冷却
     * @param [uuid] uuid
     * @param [source] 源
     * @param [time] 时间
     * @param [timeUnit] 时间单位
     */
    fun setCooling(uuid: UUID, source: String, time: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) {
        coolDatas.getOrPut(uuid) { Cooldown() }.setCooling(source, time, timeUnit)
    }

    /**
     * 是否已结束指定源的冷却时间
     * @param [uuid] uuid
     * @param [source] 源
     * @return [Boolean]
     */
    fun isTimeout(uuid: UUID, source: String): Boolean {
        return coolDatas[uuid]?.isTimeout(source) ?: true
    }

    /**
     * 获取剩余时间
     * @param [uuid] uuid
     * @param [source] 源
     * @return [Long]
     */
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