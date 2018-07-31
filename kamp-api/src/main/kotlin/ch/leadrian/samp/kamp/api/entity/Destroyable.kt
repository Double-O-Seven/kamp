package ch.leadrian.samp.kamp.api.entity

interface Destroyable {

    val isDestroyed: Boolean

    fun destroy()

}