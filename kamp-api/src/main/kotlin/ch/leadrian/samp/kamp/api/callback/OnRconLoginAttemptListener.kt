package ch.leadrian.samp.kamp.api.callback

interface OnRconLoginAttemptListener {

    fun onRconLoginAttempt(ipAddress: String, password: String, success: Boolean)

}
