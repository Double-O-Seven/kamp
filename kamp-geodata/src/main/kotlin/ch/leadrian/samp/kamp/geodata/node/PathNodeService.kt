package ch.leadrian.samp.kamp.geodata.node

import ch.leadrian.samp.kamp.core.api.util.loggerFor
import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PathNodeService
@Inject
internal constructor(private val nodeFileSource: NodeFileSource) {

    private companion object {

        const val NODE_FILE_FORMAT = "NODES%d.DAT"

        val log = loggerFor<PathNodeService>()
    }

    private val nodeFiles: MutableList<NodeFile> = mutableListOf()
    /**
     * Indexed by area ID and node ID
     */
    private val pathNodeIndex: Table<Int, Int, PathNode> = HashBasedTable.create<Int, Int, PathNode>()

    @PostConstruct
    internal fun initialize() {
        log.info("Loading path nodes...")
        loadNodeFiles()
        createPathNodeIndex()
        createLinks()
        log.info("Path nodes loaded")
    }

    fun getPathNodes(): List<PathNode> = pathNodeIndex.values().toList()

    private fun loadNodeFiles() {
        nodeFiles += (0 until 64)
                .asSequence()
                .map { String.format(NODE_FILE_FORMAT, it) }
                .map { parseNodeFile(it) }
                .toList()
    }

    private fun parseNodeFile(fileName: String): NodeFile {
        try {
            return NodeFile.parse(nodeFileSource.getNodeFileContent(fileName))
        } catch (e: Exception) {
            log.error("Failed to parse {}: ", fileName, e)
            throw IllegalStateException("Failed to parsed $fileName", e)
        }
    }

    private fun createPathNodeIndex() {
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
