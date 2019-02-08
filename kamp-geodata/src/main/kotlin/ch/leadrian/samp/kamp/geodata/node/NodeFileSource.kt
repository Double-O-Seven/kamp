package ch.leadrian.samp.kamp.geodata.node

import java.io.InputStream

interface NodeFileSource {

    fun getNodeFileContent(fileName: String): InputStream

}