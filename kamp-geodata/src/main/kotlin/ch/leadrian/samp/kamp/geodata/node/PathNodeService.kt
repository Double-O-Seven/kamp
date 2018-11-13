package ch.leadrian.samp.kamp.geodata.node

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PathNodeService
@Inject
internal constructor() {

    private companion object {

        const val NODE_FILE_FORMAT = "nodes%d.dat"
    }

    private lateinit var nodeFiles: List<NodeFile>
    /**
     * Indexed by area ID and node ID
     */
    private lateinit var pathNodeIndex: Table<Int, Int, PathNode>

    @PostConstruct
    internal fun initialize() {
        loadNodeFiles()
        createPathNodeIndex()
        createLinks()
    }

    fun getPathNodes(): List<PathNode> = pathNodeIndex.values().toList()

    private fun loadNodeFiles() {
        nodeFiles = (0 until 64)
                .asSequence()
                .map { String.format(NODE_FILE_FORMAT, it) }
                .map { javaClass.getResourceAsStream(it) }
                .map { NodeFile.parse(it) }
                .toList()
    }

    private fun createPathNodeIndex() {
        pathNodeIndex = HashBasedTable.create<Int, Int, PathNode>()
        nodeFiles
                .asSequence()
                .map { it.pathNodes }
                .flatMap { it.asSequence() }
                .forEach { addPathNodeToIndex(it) }
    }

    private fun addPathNodeToIndex(pathNode: PathNode) {
        val existing = pathNodeIndex.put(pathNode.areaId, pathNode.nodeId, pathNode)
        if (existing != null) {
            throw IllegalStateException("Duplicate path node for area ID ${pathNode.areaId} and node ID ${pathNode.nodeId}")
        }
    }

    private fun createLinks() {
        nodeFiles.forEach { createLinks(it) }
    }

    private fun createLinks(nodeFile: NodeFile) {
        nodeFile.pathNodes.forEach { addLinks(it, nodeFile) }
    }

    private fun addLinks(pathNode: PathNode, nodeFile: NodeFile) {
        for (i in 0 until pathNode.linkCount) {
            val linkId = i + pathNode.linkId
            val link = nodeFile.links[linkId]
            val linkedPathNode = pathNodeIndex.get(link.areaId, link.nodeId)
                    ?: throw IllegalStateException("Non-existant path node for area ID ${link.areaId} and node ID ${link.nodeId}")

            pathNode.addLink(linkedPathNode, link.length)
        }
    }

}
