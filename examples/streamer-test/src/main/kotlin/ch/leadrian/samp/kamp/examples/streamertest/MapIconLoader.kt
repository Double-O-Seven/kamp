package ch.leadrian.samp.kamp.examples.streamertest

import ch.leadrian.samp.kamp.core.api.constants.MapIconType
import ch.leadrian.samp.kamp.streamer.api.service.StreamableMapIconService
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MapIconLoader
@Inject
constructor(private val streamableMapIconService: StreamableMapIconService) {

    @PostConstruct
    fun initialize() {
        MapIconType.values().forEach { type ->
            type.defaultCoordinates.forEach { coordinates ->
                streamableMapIconService.createStreamableMapIcon(coordinates, type)
            }
        }
    }

}