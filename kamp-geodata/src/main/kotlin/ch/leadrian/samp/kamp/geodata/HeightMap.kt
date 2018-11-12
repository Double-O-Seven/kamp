package ch.leadrian.samp.kamp.geodata

import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.EOFException
import java.io.InputStream
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeightMap
@Inject
internal constructor() {

    private companion object {

        val log = loggerFor<HeightMap>()

    }

    private lateinit var values: FloatArray

    @PostConstruct
    internal fun initialize() {
        log.info("Loading height map...")
        HeightMap::class.java.getResourceAsStream("SAfull.hmap").use { load(it) }
        log.info("Height map loaded")
    }

    private fun load(inputStream: InputStream) {
        values = FloatArray(6000 * 6000)
        DataInputStream(BufferedInputStream(inputStream)).use {
            for (i in values.indices) {
                val char1 = it.read() and 0xFF
                val char2 = it.read() and 0xFF
                if ((char1 or char2) < 0) {
                    throw EOFException()
                }
                val value = (char2 shl 8) + char2
                values[i] = value.toFloat() / 100f
            }
        }
    }

    fun findZ(coordinates: Vector2D): Float = findZ(x = coordinates.x, y = coordinates.y)

    fun findZ(x: Float, y: Float): Float {
        if (x < -3000.0f || x > 3000.0f || y > 3000.0f || y < -3000.0f) {
            return 0.0f
        }

        val gridX = Math.round(x) + 3000
        val gridY = -(Math.round(y) - 3000)

        val heightMapPos = gridY * 6000 + gridX

        return values.getOrNull(heightMapPos) ?: 0f
    }

}