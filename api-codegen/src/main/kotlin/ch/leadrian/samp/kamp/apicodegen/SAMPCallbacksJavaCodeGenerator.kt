package ch.leadrian.samp.kamp.apicodegen

import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.cidl.parser.InterfaceDefinitionParser
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime

class SAMPCallbacksJavaCodeGenerator {

    fun generate(outputDirectory: Path, functions: List<Function>, packageName: String, className: String = "SAMPCallbacks") {
        val packageDirectoryPath = outputDirectory.resolve(packageName.replace('.', File.separatorChar))
        Files.createDirectories(packageDirectoryPath)
        val outputFile = packageDirectoryPath.resolve("$className.java")

        Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use { writer ->
            writer.write("""
                |package $packageName;
                |
                |import javax.annotation.Generated;
                |import org.jetbrains.annotations.NotNull;
                |
                |@Generated(
                |    value = "${this::class.java.name}",
                |    date = "${LocalDateTime.now()}"
                |)
                |public interface $className {
                |
                |    void onProcessTick();
                |
                |
            """.trimMargin("|"))

            functions
                    .filter { it.hasAttribute("callback") }
                    .forEach {
                        val returnJavaType = getJavaType(it.type)
                        if (!isPrimitiveJavaType(returnJavaType)) {
                            writer.write("    @NotNull\n")
                        }
                        val camelCaseName = "${it.name[0].toLowerCase()}${it.name.substring(1)}"
                        writer.write("    $returnJavaType $camelCaseName (")
                        val parameters = it.parameters.joinToString(separator = ", ") {
                            val parameterJavaType = getJavaType(it.type)
                            val notNullAnnotation = when {
                                isPrimitiveJavaType(parameterJavaType) -> ""
                                else -> "@NotNull "
                            }
                            "$notNullAnnotation$parameterJavaType ${it.name}"
                        }
                        writer.write(parameters)
                        writer.write(");\n\n")
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
            val functions = interfaceDefinitionParser.parse(*codeGeneratorArguments.interfaceDefinitionSources).functions
            SAMPCallbacksJavaCodeGenerator().generate(
                    outputDirectory = codeGeneratorArguments.outputDirectoryPath,
                    functions = functions,
                    packageName = codeGeneratorArguments.packageName
            )
        }
    }

}