package me.mkbaka.executableblock.api.block

interface Trigger<T> {

    fun call(event: EventAdapter)

    fun bindToEvent()

    val eventClass: Class<out T>

}