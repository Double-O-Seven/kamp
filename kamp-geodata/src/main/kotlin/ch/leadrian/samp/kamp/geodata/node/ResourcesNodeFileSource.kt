package ch.leadrian.samp.kamp.geodata.node

import java.io.InputStream

class ResourcesNodeFileSource(private val clazz: Class<*>) : NodeFileSource {

    override fun getNodeFileContent(fileName: String): InputStream = clazz.getResourceAsStream(fileName)

}