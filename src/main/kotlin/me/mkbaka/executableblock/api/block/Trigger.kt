package me.mkbaka.executableblock.api.block

interface Trigger<T> {

    fun call(event: T): Boolean

    fun callGlobal(event: T): Boolean

    fun bindToEvent()

    val eventClass: Class<out Any>

}