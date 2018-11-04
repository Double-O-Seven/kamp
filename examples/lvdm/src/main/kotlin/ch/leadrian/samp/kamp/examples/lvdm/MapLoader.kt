package ch.leadrian.samp.kamp.examples.lvdm

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.streamer.api.service.StreamerService
import java.io.InputStreamReader
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MapLoader
@Inject
constructor(private val streamerService: StreamerService) {

    @PostConstruct
    fun loadMap() {
        this::class.java.getResourceAsStream("objects.csv").use { inputStream ->
            InputStreamReader(inputStream).use { reader ->
                reader.readLines().filter { it.isNotBlank() }.forEach {
                    val values = it.split(',')
                    streamerService.createStreamableMapObject(
                            values[0].toInt(),
                            0,
                            300f,
                            vector3DOf(x = values[1].toFloat(), y = values[2].toFloat(), z = values[3].toFloat()),
                            vector3DOf(x = values[4].toFloat(), y = values[5].toFloat(), z = values[6].toFloat())
                    )
                }
            }
        }
    }

}