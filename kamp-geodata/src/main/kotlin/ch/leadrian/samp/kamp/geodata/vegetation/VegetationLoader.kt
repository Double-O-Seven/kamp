package ch.leadrian.samp.kamp.geodata.vegetation

import ch.leadrian.samp.kamp.core.api.data.vector3DOf
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import java.io.InputStreamReader
import javax.inject.Inject

class VegetationLoader
@Inject
constructor() {

    private companion object {

        val log = loggerFor<VegetationLoader>()

    }

    fun load(processor: VegetationProcessor) {
        log.info("Loading vegetation using $processor...")
        InputStreamReader(this::class.java.getResourceAsStream("objects.csv")).use { reader ->
            reader.readLines().filter { it.isNotBlank() }.forEach {
                val values = it.split(',')
                val vegetationObject = VegetationObject(
                        modelId = values[0].toInt(),
                        coordinates = vector3DOf(
                                x = values[1].toFloat(),
                                y = values[2].toFloat(),
                                z = values[3].toFloat()
                        ),
                        rotation = vector3DOf(x = values[4].toFloat(), y = values[5].toFloat(), z = values[6].toFloat())
                )
                processor.process(vegetationObject)
            }
        }
        log.info("Vegetation loaded")
    }

    inline fun load(crossinline processor: (VegetationObject) -> Unit) {
        load(object : VegetationProcessor {

            override fun process(vegetationObject: VegetationObject) {
                processor(vegetationObject)
            }

        })
    }

}