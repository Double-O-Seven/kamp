package ch.leadrian.samp.kamp.geodata.node

import ch.leadrian.samp.kamp.core.api.data.Vector2D
import ch.leadrian.samp.kamp.geodata.util.uncompressToVector2D
import com.google.common.io.LittleEndianDataInputStream

data class NaviNode(
        val position: Vector2D,
        val areaId: Int,
        val nodeId: Int,
        val direction: Vector2D,
        val flags: Int
) {

    internal companion object {

        fun parse(inputStream: LittleEndianDataInputStream): NaviNode {
            val positionX = inputStream.readShort()
            val positionY = inputStream.readShort()
            val position = uncompressToVector2D(x = positionX, y = positionY)
            val areaId = inputStream.readUnsignedShort()
            val nodeId = inputStream.readUnsignedShort()
            val directionX = inputStream.readByte().toShort()
            val directionY = inputStream.readByte().toShort()
            val direction = uncompressToVector2D(x = directionX, y = directionY)
            val flags = inputStream.readInt()
            return NaviNode(
                    position = position,
                    areaId = areaId,
                    nodeId = nodeId,
                    direction = direction,
                    flags = flags
            )
        }

    }

}
