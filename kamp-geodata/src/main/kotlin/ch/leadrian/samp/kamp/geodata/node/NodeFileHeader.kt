package ch.leadrian.samp.kamp.geodata.node

import com.google.common.io.LittleEndianDataInputStream

internal data class NodeFileHeader(
        val numberOfNodes: Int,
        val numberOfVehicleNodes: Int,
        val numberOfPedNodes: Int,
        val numberOfNaviNodes: Int,
        val numberOfLinks: Int
) {

    companion object {

        fun parse(inputStream: LittleEndianDataInputStream): NodeFileHeader {
            val numberOfNodes = inputStream.readInt()
            val numberOfVehicleNodes = inputStream.readInt()
            val numberOfPedNodes = inputStream.readInt()
            val numberOfNaviNodes = inputStream.readInt()
            val numberOfLinks = inputStream.readInt()
            return NodeFileHeader(
                    numberOfNodes = numberOfNodes,
                    numberOfVehicleNodes = numberOfVehicleNodes,
                    numberOfPedNodes = numberOfPedNodes,
                    numberOfNaviNodes = numberOfNaviNodes,
                    numberOfLinks = numberOfLinks
            )
        }

    }

}