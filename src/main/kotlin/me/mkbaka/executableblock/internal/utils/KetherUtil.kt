package me.mkbaka.executableblock.internal.utils

import taboolib.library.kether.ParsedAction
import taboolib.library.kether.QuestReader
import taboolib.module.kether.expects
import taboolib.module.kether.literalAction

/**
 * @author https://gitee.com/amazing-ocean-origin/planners
 */

fun QuestReader.nextArgumentActionOrNull(array: Array<out String>): ParsedAction<*>? {
    return try {
        mark()
        expects(*array)
        this.nextParsedAction()
    } catch (e: Exception) {
        reset()
        null
    }
}

fun QuestReader.nextArgumentAction(array: Array<out String>, def: Any? = null): ParsedAction<*>? {
    return nextArgumentActionOrNull(array) ?: if (def == null) null else literalAction(def)
}