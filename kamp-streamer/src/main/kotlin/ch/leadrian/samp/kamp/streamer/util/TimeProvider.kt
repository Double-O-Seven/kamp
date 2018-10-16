package ch.leadrian.samp.kamp.streamer.util

import javax.inject.Inject

internal class TimeProvider
@Inject
constructor() {

    fun getCurrentTimeInMs(): Long = System.currentTimeMillis()

}