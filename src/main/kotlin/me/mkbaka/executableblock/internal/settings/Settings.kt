package me.mkbaka.executableblock.internal.settings

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile

object Settings {

    @Config("settings.yml")
    lateinit var settings: ConfigFile

    var updatePeriod: Long = 0

    @Awake(LifeCycle.ACTIVE)
    fun active() {
        settings.onReload {
            updatePeriod = settings.getLong("Storage.period") * 20
        }
        reload()
    }

    fun reload() {
        settings.reload()
        SettingManager.callReload()
    }
}