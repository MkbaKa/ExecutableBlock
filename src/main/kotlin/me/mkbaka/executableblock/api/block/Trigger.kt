package me.mkbaka.executableblock.api.block

interface Trigger {

    fun call(event: EventAdapter)

    fun bindToEvent()

}