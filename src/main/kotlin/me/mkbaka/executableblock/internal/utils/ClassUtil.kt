package me.mkbaka.executableblock.internal.utils

import org.bukkit.event.Event
import java.lang.reflect.Modifier
import java.util.concurrent.ConcurrentHashMap

object ClassUtil {

    private val classes = ConcurrentHashMap<String, Class<*>>()

    fun String.getClass(): Class<*> {
        return try {
            classes.computeIfAbsent(this) {
                Class.forName(this)
            }
        } catch (e: Throwable) {
            error("未找到名为 $this 的事件类, 请检查拼写是否错误")
        }
    }

    fun String.toBukkitEvent(): Class<out Event> {
        return if (getClass().isBukkitEvent()) getClass() as Class<out Event> else error("类 $this 不是 Event 的子类或为抽象类!, 无法监听.")
    }

    fun Class<*>.isBukkitEvent(): Boolean {
        return Event::class.java.isAssignableFrom(this) && this.simpleName != "Event" && !Modifier.isAbstract(this.modifiers)
    }

}