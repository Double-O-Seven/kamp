package ch.leadrian.samp.kamp.geodata

import ch.leadrian.samp.kamp.core.api.inject.KampModule

internal class GeodataModule : KampModule() {

    override fun configure() {
        bind(HeightMap::class.java).asEagerSingleton()
    }

}