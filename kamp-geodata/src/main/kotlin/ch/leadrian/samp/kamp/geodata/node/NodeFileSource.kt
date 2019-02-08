package ch.leadrian.samp.kamp.geodata.node

import java.io.InputStream

/**
 * @see [DataDirectoryNodeFileSource]
 * @see [ResourcesNodeFileSource]
 */
interface NodeFileSource {

    fun getNodeFileContent(fileName: String): InputStream

}