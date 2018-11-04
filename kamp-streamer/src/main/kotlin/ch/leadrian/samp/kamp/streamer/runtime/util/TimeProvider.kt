package ch.leadrian.samp.kamp.streamer.runtime.util

import javax.inject.Inject

internal class TimeProvider
@Inject
constructor() {

    fun getCurrentTimeInMs(): Long = System.currentTimeMillis()

}