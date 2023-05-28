package me.mkbaka.executableblock.internal.settings

import me.mkbaka.executableblock.internal.hook.nashorn.CompiledScriptFactory
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object Settings {

    @Config("settings.yml")
    lateinit var settings: ConfigFile
    var updatePeriod: Long = 0
    var enableRegionListener: Boolean = false

    @Awake(LifeCycle.ACTIVE)
    fun active() {
        settings.onReload {
            updatePeriod = settings.getLong("Storage.period") * 20
            enableRegionListener = settings.getBoolean("Region.listener")
        }
        reload()
    }

    fun reload() {
        settings.reload()
        SettingManager.callReload()
        CompiledScriptFactory.reload()
    }
}