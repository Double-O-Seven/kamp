package ch.leadrian.samp.kamp.geodata.node

import com.google.common.io.LittleEndianDataInputStream

internal class Link {

    var areaId: Int = 0
        private set

    var nodeId: Int = 0
        private set

    var naviNodeId: Int = 0
        private set

    var naviAreaId: Int = 0
        private set

    var length: Int = 0
        private set

    internal fun parseLink(input: LittleEndianDataInputStream) {
        areaId = input.readUnsignedShort()
        nodeId = input.readUnsignedShort()
    }

    internal fun parseNaviLink(input: LittleEndianDataInputStream) {
        val value = input.readUnsignedShort()
        naviNodeId = value and 0b11_1111_1111
        naviAreaId = (value shr 6) and 0b11_1111
    }

    internal fun parseLength(input: LittleEndianDataInputStream) {
        length = input.readUnsignedByte()
    }
}