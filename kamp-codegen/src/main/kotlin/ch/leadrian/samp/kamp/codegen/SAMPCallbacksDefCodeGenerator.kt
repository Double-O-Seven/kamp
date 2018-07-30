package ch.leadrian.samp.kamp.codegen

import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.cidl.parser.InterfaceDefinitionParser
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class SAMPCallbacksDefCodeGenerator {

    fun generate(outputDirectory: Path, functions: List<Function>, fileName: String = "SAMPCallbacks") {
        Files.createDirectories(outputDirectory)
        val outputFile = outputDirectory.resolve("$fileName.def")

        Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use { writer ->
            writer.write("EXPORTS\n")
            writer.write("""
                |    Supports
                |    Load
                |    Unload
                |    ProcessTick
                |""".trimMargin())
            functions.filter { it.hasAttribute("callback") }.forEach {
                writer.write("    ${it.name}\n")
            }
            writer.close()
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val codeGeneratorArguments = CodeGeneratorArguments.parse(args = args, packageNameRequired = false)
            val interfaceDefinitionParser = InterfaceDefinitionParser()
            val functions = interfaceDefinitionParser.parse(*codeGeneratorArguments.interfaceDefinitionSources).functions
            SAMPCallbacksDefCodeGenerator().generate(
                    outputDirectory = codeGeneratorArguments.outputDirectoryPath,
                    functions = functions
            )
        }
    }
}