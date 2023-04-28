package me.mkbaka.executableblock.internal.autoregister

import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import kotlin.reflect.KClass

@RuntimeDependencies(
    RuntimeDependency("!javassist:javassist:3.12.1.GA", test = "javassist.bytecode.ClassFile"),
    RuntimeDependency("!org.reflections:reflections:0.10.2", test = "org.reflections.Reflections")
)
class AutoRegisterScanner(
    private val classLoader: ClassLoader
) {

    fun scanClasses(basePackageName: String): List<KClass<*>> {
        val reflections = Reflections(
            ConfigurationBuilder().apply {
                addClassLoaders(classLoader)
                forPackage(basePackageName, classLoader)
                filterInputsBy(FilterBuilder().apply {
                    includePackage(basePackageName)
                })
            }
        )

        return reflections.getTypesAnnotatedWith(AutoRegister::class.java).map {
            it.kotlin
        }
    }

}