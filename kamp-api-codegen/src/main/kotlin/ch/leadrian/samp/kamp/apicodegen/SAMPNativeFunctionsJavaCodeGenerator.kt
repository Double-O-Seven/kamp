package ch.leadrian.samp.kamp.apicodegen

import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.cidl.parser.InterfaceDefinitionParser
import java.io.BufferedWriter
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime

class SAMPNativeFunctionsJavaCodeGenerator {

    fun generate(outputDirectory: Path, functions: List<Function>, packageName: String, className: String = "SAMPNativeFunctions") {
        val packageDirectoryPath = outputDirectory.resolve(packageName.replace('.', File.separatorChar))
        Files.createDirectories(packageDirectoryPath)
        val outputFile = packageDirectoryPath.resolve("$className.java")

        Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use { writer ->
            writeHeader(writer, packageName, className)
            writeFunctions(functions, writer)
            writeFooter(writer)
        }
    }

    private fun writeHeader(writer: BufferedWriter, packageName: String, className: String) {
        writer.write("""
            |
            |package $packageName;
            |
            |import javax.annotation.Generated;
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
            |""".trimMargin("|"))
    }

    private fun writeFunctions(functions: List<Function>, writer: BufferedWriter) {
        functions
                .filter { it.hasAttribute("native") && !it.hasAttribute("noimpl") }
                .forEach { writeFunction(it, writer) }
    }

    private fun writeFunction(it: Function, writer: BufferedWriter) {
        val returnJavaType = getJavaType(it.type)
        if (!isPrimitiveJavaType(returnJavaType)) {
            writer.write("    @NotNull\n")
        }
        val camelCaseName = "${it.name[0].toLowerCase()}${it.name.substring(1)}"
        writer.write("    public static native $returnJavaType $camelCaseName (")
        val parameters = it.parameters.joinToString(separator = ", ") {
            val parameterJavaType = when {
                it.hasAttribute("out") -> getJavaOutType(it.type)
                else -> getJavaType(it.type)
            }
            val notNullAnnotation = when {
                isPrimitiveJavaType(parameterJavaType) -> ""
                else -> "@NotNull "
            }
            "$notNullAnnotation$parameterJavaType ${it.name}"
        }
        writer.write(parameters)
        writer.write(");\n\n")
    }

    private fun writeFooter(writer: BufferedWriter) {
        writer.write("}\n")
        writer.close()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val codeGeneratorArguments = CodeGeneratorArguments.parse(args)
            val interfaceDefinitionParser = InterfaceDefinitionParser()
            val functions = interfaceDefinitionParser.parse(*codeGeneratorArguments.interfaceDefinitionSources).functions
            SAMPNativeFunctionsJavaCodeGenerator().generate(
                    outputDirectory = codeGeneratorArguments.outputDirectoryPath,
                    functions = functions,
                    packageName = codeGeneratorArguments.packageName
            )
        }
    }

}