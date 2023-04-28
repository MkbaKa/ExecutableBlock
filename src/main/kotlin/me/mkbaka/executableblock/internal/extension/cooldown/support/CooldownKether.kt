package me.mkbaka.executableblock.internal.extension.cooldown.support

import me.mkbaka.executableblock.command.CoolCommand
import me.mkbaka.executableblock.command.CoolCommand.editCool
import taboolib.common.platform.function.info
import taboolib.common5.clong
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class CooldownKether {

    class Get(val source: String) : ScriptAction<Long>() {

        override fun run(frame: ScriptFrame): CompletableFuture<Long> {
            return CompletableFuture.completedFuture(editCool(CoolCommand.EditType.GET, frame.player().uniqueId, source))
        }

    }

    class Add(val source: String, val time: Long, val timeUnit: TimeUnit = TimeUnit.MILLISECONDS) : ScriptAction<Void>() {

        override fun run(frame: ScriptFrame): CompletableFuture<Void> {
            editCool(CoolCommand.EditType.ADD, frame.player().uniqueId, source, time, timeUnit)
            return CompletableFuture.completedFuture(null)
        }

    }

    class Take(val source: String, val time: Long, val timeUnit: TimeUnit = TimeUnit.MILLISECONDS) : ScriptAction<Void>() {

        override fun run(frame: ScriptFrame): CompletableFuture<Void> {
            editCool(CoolCommand.EditType.TAKE, frame.player().uniqueId, source, time, timeUnit)
            return CompletableFuture.completedFuture(null)
        }

    }

    companion object {

        /**
         * cooldown [action] [source] <time>
         */
        @KetherParser(["cooldown"], namespace = "ExecutableBlock", shared = true)
        fun parser() = scriptParser {
            val action = it.nextToken()
            val source = it.nextToken()

            when (action.lowercase()) {
                "get" -> Get(source)
                "add" -> Add(source, it.nextLong())
                "take" -> Take(source, it.nextLong())
                else -> error("Unknow action type $action")
            }
        }
    }
}