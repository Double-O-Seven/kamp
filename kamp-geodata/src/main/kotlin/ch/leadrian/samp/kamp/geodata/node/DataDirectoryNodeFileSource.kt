package ch.leadrian.samp.kamp.geodata.node

import ch.leadrian.samp.kamp.geodata.GeodataPlugin
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import javax.inject.Inject

class DataDirectoryNodeFileSource
@Inject
constructor(private val geodataPlugin: GeodataPlugin) : NodeFileSource {

    override fun getNodeFileContent(fileName: String): InputStream {
        val dataDirectory = geodataPlugin.dataDirectory
        val nodeFile = dataDirectory.resolve(fileName)
        return Files.newInputStream(nodeFile, StandardOpenOption.READ)
    }

}