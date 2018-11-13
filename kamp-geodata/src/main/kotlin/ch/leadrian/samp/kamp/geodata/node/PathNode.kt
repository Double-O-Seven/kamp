package ch.leadrian.samp.kamp.geodata.node

import ch.leadrian.samp.kamp.core.api.data.Vector3D
import ch.leadrian.samp.kamp.core.api.data.rectangleOf
import ch.leadrian.samp.kamp.core.api.util.getBit
import ch.leadrian.samp.kamp.geodata.util.uncompress
import ch.leadrian.samp.kamp.geodata.util.uncompressToVector3D
import com.google.common.io.LittleEndianDataInputStream
import java.util.Collections.unmodifiableList

data class PathNode(
        val isPedPathNode: Boolean,
        val position: Vector3D,
        val linkId: Int,
        val areaId: Int,
        val nodeId: Int,
        val width: Float,
        val type: PathNodeType,
        val flags: Int,
        private val _links: MutableList<PathNodeLink> = mutableListOf()
) {

    internal companion object {

        fun parse(isPedPathNode: Boolean, inputStream: LittleEndianDataInputStream): PathNode {
            inputStream.skipBytes(8)

            val x: Short = inputStream.readShort()
            val y: Short = inputStream.readShort()
            val z: Short = inputStream.readShort()
            val position = uncompressToVector3D(x = x, y = y, z = z)

            inputStream.skipBytes(2)

            val linkId = inputStream.readUnsignedShort()
            val areaId = inputStream.readUnsignedShort()
            val nodeId = inputStream.readUnsignedShort()

            val width = inputStream.readUnsignedByte().uncompress()
            val type = PathNodeType.fromFloodFill(inputStream.readByte().toInt()) // Flood fill
            val flags = inputStream.readInt()

            if (position !in rectangleOf(minX = -3000f, minY = -3000f, maxX = 3000f, maxY = 3000f)) {
                throw IllegalArgumentException("Invalid position $position")
            }

            return PathNode(
                    isPedPathNode = isPedPathNode,
                    position = position,
                    linkId = linkId,
                    areaId = areaId,
                    nodeId = nodeId,
                    width = width,
                    type = type,
                    flags = flags
            )
        }
    }

    val links: List<PathNodeLink> = unmodifiableList(_links)

    internal fun addLink(pathNode: PathNode, length: Int) {
        _links += PathNodeLink(pathNode, length)
    }

    val linkCount: Int = flags and 0b1111

    val isEmergencyVehiclesOnly: Boolean
        get() = flags.getBit(8) != 0

    val isHighway: Boolean
        get() = flags.getBit(13) != 0

    val isNotHighway: Boolean
        get() = flags.getBit(12) != 0

    val isBoat: Boolean
        get() = flags.getBit(7) != 0

    val isParking: Boolean
        get() = flags.getBit(21) != 0

}
