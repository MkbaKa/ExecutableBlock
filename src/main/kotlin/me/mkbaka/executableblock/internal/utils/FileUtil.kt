package me.mkbaka.executableblock.internal.utils

import java.io.File

object FileUtil {

    fun File.executeSubFiles(callback: (File) -> Unit) {
        listFiles()?.forEach {
            if (it.isDirectory) it.executeSubFiles(callback)
                else callback(it)
        }
    }

}