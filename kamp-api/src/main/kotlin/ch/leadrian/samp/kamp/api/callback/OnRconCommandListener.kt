package ch.leadrian.samp.kamp.api.callback

interface OnRconCommandListener {

    fun onRconCommand(command: String): Boolean

}
