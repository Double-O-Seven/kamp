package ch.leadrian.samp.kamp.geodata

import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.geodata.hmap.HeightMap
import ch.leadrian.samp.kamp.geodata.node.PathNodeService

internal class GeodataModule : KampModule() {

    override fun configure() {
        // Need to load data on start up since it is computationally intensive
        bind(HeightMap::class.java).asEagerSingleton()
        bind(PathNodeService::class.java).asEagerSingleton()
    }

}