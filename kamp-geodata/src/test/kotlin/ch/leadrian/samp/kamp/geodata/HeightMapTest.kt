package ch.leadrian.samp.kamp.geodata

import com.google.common.io.Resources
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.min

internal class HeightMapTest {

    @Test
    @Throws(IOException::class)
    fun shouldLoadHeightMap() {
        val map = HeightMap().apply { initialize() }
        val outputStream = ByteArrayOutputStream()

        writeImage(map, outputStream)

        val expectedImageBytes = Resources.toByteArray(HeightMap::class.java.getResource("SAfull.png"))
        assertThat(outputStream.toByteArray())
                .isEqualTo(expectedImageBytes)
    }

    private fun writeImage(map: HeightMap, outputStream: OutputStream) {
        val maxZ = getMaxZ(map)
        val image = BufferedImage(6000, 6000, BufferedImage.TYPE_BYTE_GRAY)
        for (y in 0..5999) {
            for (x in 0..5999) {
                val z = map.findZ(x - 3000f, y - 3000f)
                val s = Math.round((z * 255f) / maxZ)
                val i = max(0, min(s, 255)) and 0xFF
                image.raster.setSample(x, 5999 - y, 0, i)
            }
        }
        ImageIO.write(image, "png", outputStream)
    }

    private fun getMaxZ(map: HeightMap): Float {
        var maxZ = 0f
        for (y in 0..5999) {
            for (x in 0..5999) {
                val z = map.findZ(x - 3000f, y - 3000f)
                maxZ = max(maxZ, z)
            }
        }
        return maxZ
    }


}