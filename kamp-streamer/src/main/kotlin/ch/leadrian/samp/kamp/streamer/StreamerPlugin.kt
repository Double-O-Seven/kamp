package ch.leadrian.samp.kamp.streamer

import ch.leadrian.samp.kamp.core.api.Plugin
import ch.leadrian.samp.kamp.streamer.runtime.StreamerModule
import com.google.inject.Module

class StreamerPlugin : Plugin() {

    override fun getModules(): List<Module> = listOf(StreamerModule())
}