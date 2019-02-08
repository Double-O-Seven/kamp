package ch.leadrian.samp.kamp.geodata.node

import com.google.common.io.Resources
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO

internal class PathNodeServiceTest {

    @Disabled
    @Test
    fun shouldLoadPathNodes() {
        val pathNodeService = PathNodeService(TestNodeFileSource)

        pathNodeService.initialize()

        val expectedImageBytes = Resources.toByteArray(PathNodeService::class.java.getResource("SanAndreasPaths.jpg"))
        val outputStream = ByteArrayOutputStream()
        val pathNodes = pathNodeService.getPathNodes()
        writeImage(pathNodes, outputStream)
        outputStream.close()
        assertThat(outputStream.toByteArray())
                .isEqualTo(expectedImageBytes)
    }

    private fun writeImage(pathNodes: List<PathNode>, outputStream: ByteArrayOutputStream) {
        val image = ImageIO.read(PathNodeServiceTest::class.java.getResourceAsStream("SanAndreasMap.jpg"))
        pathNodes.forEach { pathNode ->
            val x = Math.round(pathNode.position.x + 3000f)
            val y = Math.round(pathNode.position.y + 3000f)

            val color: Int =
                    if (pathNode.isPedPathNode) {
                        0x00FFFF
                    } else {
                        when (pathNode.type) {
                            PathNodeType.BOATS -> 0x0000FF
                            PathNodeType.OTHER -> 0x00FF00
                            PathNodeType.VEHICLE_TRAFFIC -> 0xFF0000
                            else -> 0xFF00FF
                        }
                    }
            image.setRGB(x, 5999 - y, color)
            drawLinks(pathNode, image, color)
        }
        ImageIO.write(image, "jpeg", outputStream)
    }

    private fun drawLinks(pathNode: PathNode, image: BufferedImage, color: Int) {
        pathNode
                .links
                .map { it.pathNode }
                .forEach { linkedNode ->
                    val distance = pathNode.position.distanceTo(linkedNode.position)
                    val direction = linkedNode.position - pathNode.position
                    val numSteps = Math.round(distance)
                    val stepSize = distance / numSteps

                    for (i in 0 until numSteps) {
                        val intermediatePos = (direction * (stepSize * i / distance)) + pathNode.position
                        val x = Math.round(intermediatePos.x) + 3000
                        val y = Math.round(intermediatePos.y) + 3000
                        image.setRGB(x, 5999 - y, color)
                    }
                }
    }

    private object TestNodeFileSource : NodeFileSource {

        override fun getNodeFileContent(fileName: String): InputStream =
                ByteArrayInputStream(Resources.toByteArray(javaClass.getResource(fileName)))

    }

}