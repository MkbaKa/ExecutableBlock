package me.mkbaka.executableblock

import me.mkbaka.executableblock.internal.autoregister.AutoRegisterFrame
import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.chat.colored
import taboolib.module.lang.sendLang
import taboolib.platform.BukkitPlugin

object ExecutableBlock : Plugin() {

    private lateinit var autoRegisterFrame: AutoRegisterFrame

    val plugin by lazy {
        BukkitPlugin.getInstance()
    }

    val prefix by lazy {
        "&7[&9Execut&3able&bBlock&7]".colored()
    }

    override fun onEnable() {
        autoRegisterFrame = AutoRegisterFrame(this::class.java.classLoader, this::class.java.`package`.name)
        console().sendLang("plugin-enable", prefix, Bukkit.getBukkitVersion())
    }

}