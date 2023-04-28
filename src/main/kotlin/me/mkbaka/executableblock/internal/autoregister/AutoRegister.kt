package me.mkbaka.executableblock.internal.autoregister

annotation class AutoRegister(
    val name: String,
    val alias: Array<String> = []
)