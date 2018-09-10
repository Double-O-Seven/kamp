package ch.leadrian.samp.kamp.core.api.callback

interface OnRconCommandListener {

    fun onRconCommand(command: String): Result

    sealed class Result(val value: Boolean) {

        object Processed : Result(true)

        object UnknownCommand : Result(false)
    }

}
