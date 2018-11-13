package ch.leadrian.samp.kamp.geodata.node

import com.google.common.io.LittleEndianDataInputStream
import java.io.BufferedInputStream
import java.io.InputStream

internal data class NodeFile(
        val pathNodes: List<PathNode>,
        val naviNodes: List<NaviNode>,
        val links: List<Link>
) {

    companion object {

        private const val FILLER_SIZE = 768

        fun parse(inputStream: InputStream): NodeFile {
            LittleEndianDataInputStream(BufferedInputStream(inputStream)).use { it ->
                val pathNodes = mutableListOf<PathNode>()
                val naviNodes = mutableListOf<NaviNode>()
                val links = mutableListOf<Link>()
                val header = NodeFileHeader.parse(it)

                for (i in 0 until header.numberOfVehicleNodes) {
                    val pathNode = PathNode.parse(false, it)
                    pathNodes.add(pathNode)
                }

                for (i in 0 until header.numberOfPedNodes) {
                    val pathNode = PathNode.parse(true, it)
                    pathNodes.add(pathNode)
                }

                for (i in 0 until header.numberOfNaviNodes) {
                    val naviNode = NaviNode.parse(it)
                    naviNodes.add(naviNode)
                }

                for (i in 0 until header.numberOfLinks) {
                    val link = Link()
                    link.parseLink(it)
                    links.add(link)
                }

                for (i in 0 until FILLER_SIZE) {
                    it.readByte()
                }

                for (link in links) {
                    link.parseNaviLink(it)
                }

                for (link in links) {
                    link.parseLength(it)
                }

                return NodeFile(pathNodes, naviNodes, links)
            }
        }
    }

}