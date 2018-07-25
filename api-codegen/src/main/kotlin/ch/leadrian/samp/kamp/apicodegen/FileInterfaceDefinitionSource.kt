package ch.leadrian.samp.kamp.apicodegen

import java.io.InputStream
import java.nio.file.Files
import java.nio.file.OpenOption
import java.nio.file.Path

class FileInterfaceDefinitionSource(
        private val filePath: Path,
        private vararg val options: OpenOption
) : InterfaceDefinitionSource {

    override fun getInputStream(): InputStream = Files.newInputStream(filePath, *options)

}