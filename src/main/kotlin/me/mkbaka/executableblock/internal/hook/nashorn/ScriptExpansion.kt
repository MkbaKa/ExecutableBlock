package me.mkbaka.executableblock.internal.hook.nashorn

class ScriptExpansion : CompiledScript {
    /**
     * 构建JavaScript脚本扩展
     *
     * @property script js脚本文本
     * @constructor JavaScript脚本扩展
     */
    constructor(script: String) : super(script)

    override fun loadLib() {
        scriptEngine.eval(
            """
                const Bukkit = Packages.org.bukkit.Bukkit
                const pluginManager = Bukkit.getPluginManager()
                const scheduler = Bukkit.getScheduler()
                const plugin = Packages.me.mkbaka.executableblock.ExecutableBlock.INSTANCE.plugin
                const CooldownManager = Packages.me.mkbaka.executableblock.internal.extension.cooldown.CooldownManager.INSTANCE
                const RegionManager = Packages.me.mkbaka.executableblock.internal.hook.region.RegionManager.INSTANCE
                const Util = Packages.me.mkbaka.executableblock.internal.utils.Util.INSTANCE
                const FileUtil = Packages.me.mkbaka.executableblock.internal.utils.FileUtil.INSTANCE
                const ItemUtil = Packages.me.mkbaka.executableblock.internal.utils.ItemUtil.INSTANCE
                const ClassUtil = Packages.me.mkbaka.executableblock.internal.utils.ClassUtil.INSTANCE
                
                const ItemStack = Packages.org.bukkit.inventory.ItemStack
                const Material = Packages.org.bukkit.Material
                
                const papi = function(player, str) {
                    return Util.parsePapi(str, player)
                }
                
                const colored = function(str) {
                    return Util.color(str)
                }
                
                const unColored = function(str) {
                    return Util.uncolor(str)
                }
                
                function sync(task) {
                    if (Bukkit.isPrimaryThread()) {
                        task()
                    } else {
                        scheduler.callSyncMethod(plugin, task)
                    }
                }
                
                function async(task) {
                    scheduler["runTaskAsynchronously(Plugin,Runnable)"](plugin, task)
                }
            """.trimIndent()
        )
    }
}