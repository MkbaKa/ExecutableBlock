package me.mkbaka.executableblock.internal.executes

import me.mkbaka.executableblock.api.block.Registerable
import me.mkbaka.executableblock.internal.executes.impl.KetherImpl
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass

object ExecuteManager : Registerable<Execute>() {

    override val root: KClass<Execute>
        get() = Execute::class

    /**
     * 执行脚本 一般用于判断条件
     * @param [script] 脚本
     * @param [sender] 执行目标
     * @param [args] 参数
     * @return [Boolean]
     */
    fun execute(script: String, sender: CommandSender, args: HashMap<String, Any> = hashMapOf()): Boolean {
        registers.forEach { (prefix, exec) ->
            if (script.startsWith(prefix)) {
                return exec.eval(script.removePrefix("$prefix:"), sender, args)
            }
        }
        return KetherImpl.eval(script, sender, args)
    }

    fun evalScript(script: String, sender: CommandSender, args: HashMap<String, Any> = hashMapOf()) {
        registers.forEach { (prefix, exec) ->
            if (script.startsWith(prefix)) {
                exec.evalScript(script.removePrefix("$prefix:"), sender, args)
                return
            }
        }
        KetherImpl.evalScript(script, sender, args)
    }

    /**
     * 获取脚本执行结果
     * @param [script] 脚本
     * @param [sender] 执行目标
     * @param [args] 参数
     * @return [Any?]
     */
    fun result(script: String, sender: CommandSender, args: HashMap<String, Any> = hashMapOf(), isFunc: Boolean = false): Any? {
        registers.forEach { (prefix, exec) ->
            if (script.startsWith(prefix)) {
                return exec.result(script.removePrefix("$prefix:"), sender, args, isFunc)
            }
        }
        return KetherImpl.result(script, sender, args, isFunc)
    }

}