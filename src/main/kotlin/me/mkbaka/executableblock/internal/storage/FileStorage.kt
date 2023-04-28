package me.mkbaka.executableblock.internal.storage

import me.mkbaka.executableblock.internal.storage.impl.JsonBlockStorage
import taboolib.common.io.newFile
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import java.io.File

abstract class FileStorage : Storage() {

    protected val files = HashMap<String, File>()
    protected val configs = HashMap<String, Configuration>()

    abstract val folder: File

    open val String.toStorageFile: File
        get() = files.getOrPut(this) { newFile(JsonBlockStorage.folder, "$this.json", create = true) }

    open val String.toConfiguration: Configuration
        get() = configs.getOrPut(this) { Configuration.loadFromFile(toStorageFile, Type.JSON) }
}