package me.mkbaka.executableblock.internal.extension.cooldown

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class Cooldown {

    private val coolCache = ConcurrentHashMap<String, Cooling>()

    fun isTimeout(source: String): Boolean {
        return coolCache[source]?.isTimeout ?: true
    }

    fun addCooling(source: String, time: Long, timeUnit: TimeUnit) {
        coolCache.getOrPut(source) { Cooling(System.currentTimeMillis(), timeUnit) }.addTime(time, timeUnit)
    }

    fun takeCooling(source: String, time: Long, timeUnit: TimeUnit) {
        coolCache.getOrPut(source) { Cooling(System.currentTimeMillis(), timeUnit) }.takeTime(time, timeUnit)
    }

    fun setCooling(source: String, time: Long, timeUnit: TimeUnit) {
        coolCache.getOrPut(source) { Cooling(System.currentTimeMillis(), timeUnit) }.setTime(time, timeUnit)
    }

    fun getTime(source: String): Long {
        return coolCache[source]?.remainingTime ?: 0
    }

    fun getCoolCache() = hashMapOf<String, Cooling>().apply { putAll(coolCache) }

}

data class Cooling(var totalTime: Long, val timeUnit: TimeUnit) {

    val isTimeout: Boolean
        get() = System.currentTimeMillis() >= timeUnit.toMillis(totalTime)

    val remainingTime: Long
        get() = if (isTimeout) 0 else totalTime - System.currentTimeMillis()

    fun addTime(time: Long, timeUnit: TimeUnit) {
        if (isTimeout) {
            totalTime = System.currentTimeMillis() + timeUnit.toMillis(time)
        } else {
            totalTime += timeUnit.toMillis(time)
        }
    }

    fun setTime(time: Long, timeUnit: TimeUnit) {
        totalTime = timeUnit.toMillis(time)
    }

    fun takeTime(time: Long, timeUnit: TimeUnit) {
        if (isTimeout) return
        totalTime -= timeUnit.toMillis(time)
    }

}