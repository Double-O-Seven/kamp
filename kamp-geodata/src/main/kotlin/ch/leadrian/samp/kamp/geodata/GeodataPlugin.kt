package ch.leadrian.samp.kamp.geodata

import ch.leadrian.samp.kamp.core.api.Plugin
import com.google.inject.Module

class GeodataPlugin : Plugin() {

    override fun getModules(): List<Module> = listOf(GeodataModule())
}