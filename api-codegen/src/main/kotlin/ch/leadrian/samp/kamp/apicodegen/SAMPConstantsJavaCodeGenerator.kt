package ch.leadrian.samp.kamp.apicodegen

import ch.leadrian.samp.cidl.model.Constant
import ch.leadrian.samp.cidl.parser.InterfaceDefinitionParser
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption.CREATE
import java.nio.file.StandardOpenOption.WRITE
import java.time.LocalDateTime


class SAMPConstantsJavaCodeGenerator {

    fun generate(outputDirectory: Path, constants: List<Constant>, packageName: String, className: String = "SAMPConstants") {
        val packageDirectoryPath = outputDirectory.resolve(packageName.replace('.', File.pathSeparatorChar))
        Files.createDirectories(packageDirectoryPath)
        val outputFile = packageDirectoryPath.resolve("$className.java")
        Files.newBufferedWriter(outputFile, CREATE, WRITE).use { writer ->
            writer.write("""hi
                |package $packageName;
                |
                |import javax.annotation.Generated;
                |
                |import org.jetbrains.annotations.NotNull;
                |
                |@Generated(
                |    value = "${this::class.java.name}",
                |    date = "${LocalDateTime.now()}"
                |)
                |public final class $className {
                |
                |    private $className() {}
                |
            """.trimMargin("|"))

            constants.forEach {
                val javaType = getJavaType(it.type)
                writer.write("    @NotNull")
                writer.write("    public static final $javaType ${it.name} = ${it.value.data};\n\n")
            }

            writer.write("}\n")
            writer.close()
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val codeGeneratorArguments = CodeGeneratorArguments.parse(args)
            val interfaceDefinitionParser = InterfaceDefinitionParser()
            val constants = interfaceDefinitionParser.parse(*codeGeneratorArguments.interfaceDefinitionSources).constants
            SAMPConstantsJavaCodeGenerator().generate(
                    outputDirectory = codeGeneratorArguments.outputDirectoryPath,
                    constants = constants,
                    packageName = codeGeneratorArguments.packageName
            )
        }
    }
}