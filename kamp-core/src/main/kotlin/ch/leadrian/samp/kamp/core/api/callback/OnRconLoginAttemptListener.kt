package ch.leadrian.samp.kamp.core.api.callback

interface OnRconLoginAttemptListener {

    fun onRconLoginAttempt(ipAddress: String, password: String, success: Boolean)

}
