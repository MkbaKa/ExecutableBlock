package me.mkbaka.executableblock.internal.autoregister

import me.mkbaka.executableblock.api.block.Trigger
import me.mkbaka.executableblock.internal.executes.Execute
import me.mkbaka.executableblock.internal.executes.ExecuteManager
import me.mkbaka.executableblock.internal.trigger.TriggerManager
import taboolib.common.env.RuntimeDependency
import kotlin.reflect.full.isSubclassOf

@RuntimeDependency("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
class AutoRegisterFrame(
    classLoader: ClassLoader,
    basePackageName: String
) {

    private val scanner by lazy {
        AutoRegisterScanner(classLoader)
    }

    /**
     * 半自动化注册.jpg
     */
    init {
        scanner.scanClasses(basePackageName).forEach { clazz ->
            when {
                clazz.isSubclassOf(Trigger::class) -> TriggerManager.register(clazz) {
                    it.bindToEvent()
                    TriggerManager.eventToTrigger[it.eventClass] = it
                }

                clazz.isSubclassOf(Execute::class) -> ExecuteManager.register(clazz)
            }
        }
    }

}