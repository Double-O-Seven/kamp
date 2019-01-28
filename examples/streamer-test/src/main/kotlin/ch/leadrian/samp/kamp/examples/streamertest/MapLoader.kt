package ch.leadrian.samp.kamp.examples.streamertest

import ch.leadrian.samp.kamp.geodata.vegetation.VegetationLoader
import ch.leadrian.samp.kamp.geodata.vegetation.VegetationObject
import ch.leadrian.samp.kamp.geodata.vegetation.VegetationProcessor
import ch.leadrian.samp.kamp.streamer.api.service.StreamableMapObjectService
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MapLoader
@Inject
constructor(
        private val streamableMapObjectService: StreamableMapObjectService,
        private val vegetationLoader: VegetationLoader
) : VegetationProcessor {

    @PostConstruct
    fun loadMap() {
        vegetationLoader.load(this)
    }

    override fun process(vegetationObject: VegetationObject) {
        streamableMapObjectService.createStreamableMapObject(
                modelId = vegetationObject.modelId,
                priority = 0,
                streamDistance = 300f,
                coordinates = vegetationObject.coordinates,
                rotation = vegetationObject.rotation
        )
    }

}