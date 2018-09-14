package ch.leadrian.samp.kamp.codegen

import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.cidl.parser.InterfaceDefinitionParser
import java.io.BufferedWriter
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime

class SAMPNativeFunctionExecutorImplKotlinCodeGenerator {

    fun generate(outputDirectory: Path, functions: List<Function>, packageName: String, className: String = "SAMPNativeFunctionExecutor") {
        val packageDirectoryPath = outputDirectory.resolve(packageName.replace('.', File.separatorChar))
        Files.createDirectories(packageDirectoryPath)
        val outputFile = packageDirectoryPath.resolve("${className}Impl.kt")

        Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use { writer ->
            writeHeader(writer, packageName, className)
            writeFunctions(functions, writer)
            writeFooter(writer)
        }
    }

    private fun writeHeader(writer: BufferedWriter, packageName: String, className: String) {
        writer.write("""
            |
            |package $packageName
            |
            |import javax.annotation.Generated
            |
            |@Generated(
            |    value = ["${this::class.java.name}"],
            |    date = "${LocalDateTime.now()}"
            |)
            |class ${className}Impl: $className {
            |
            |    private lateinit var mainThread: Thread
            |
            |    private var isInitialized = false
            |
            |    @Synchronized
            |    override fun initialize() {
            |        if (isInitialized) return
            |
            |        mainThread = Thread.currentThread()
            |        isInitialized = true
            |    }
            |
            |    private inline fun <T> requireOnMainThread(block: () -> T): T {
            |        if (Thread.currentThread() != mainThread) {
            |            throw IllegalStateException("Can only execute native functions on main thread")
            |        }
            |        return block()
            |    }
            |
            |""".trimMargin("|"))
    }

    private fun writeFunctions(functions: List<Function>, writer: BufferedWriter) {
        functions
                .filter { it.hasAttribute("native") && !it.hasAttribute("noimpl") }
                .forEach { writeFunction(it, writer) }
    }

    private fun writeFunction(it: Function, writer: BufferedWriter) {
        val kotlinReturnType = getKotlinType(it.type)
        val camelCaseName = "${it.name[0].toLowerCase()}${it.name.substring(1)}"
        writer.write("    override fun $camelCaseName (")
        val parameters = it.parameters.joinToString(separator = ", ") {
            val parameterJavaType = when {
                it.hasAttribute("out") -> getKotlinOutType(it.type)
                else -> getKotlinType(it.type)
            }
            "${it.name}: $parameterJavaType"
        }
        writer.write(parameters)
        writer.write("): $kotlinReturnType = requireOnMainThread {\n        SAMPNativeFunctions.$camelCaseName(")
        writer.write(it.parameters.joinToString(separator = ", ") { it.name })
        writer.write(")\n    }\n\n")
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
            SAMPNativeFunctionExecutorImplKotlinCodeGenerator().generate(
                    outputDirectory = codeGeneratorArguments.outputDirectoryPath,
                    functions = functions,
                    packageName = codeGeneratorArguments.packageName
            )
        }
    }

}