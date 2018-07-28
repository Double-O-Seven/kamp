package ch.leadrian.samp.kamp.runtimecodegen

import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.cidl.model.Types
import ch.leadrian.samp.cidl.parser.InterfaceDefinitionParser
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class SAMPNativeFunctionsCppCodeGenerator {

    fun generate(outputDirectory: Path, functions: List<Function>, packageName: String, fileName: String = "SAMPNativeFunctions") {
        Files.createDirectories(outputDirectory)
        val outputFile = outputDirectory.resolve("$fileName.cpp")

        Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use { writer ->
            writeHeader(writer, fileName)
            writeFunctions(functions, packageName, writer)
            writeFooter(writer)
        }
    }

    private fun writeHeader(writer: BufferedWriter, fileName: String) {
        writer.write("""
            |
            |#include "$fileName.h"
            |
            |""".trimMargin("|"))
    }

    private fun writeFunctions(functions: List<Function>, packageName: String, writer: BufferedWriter) {
        functions
                .filter { it.hasAttribute("native") && !it.hasAttribute("noimpl") }
                .forEach { writeFunction(it, packageName, writer) }
    }

    private fun writeFunction(function: Function, packageName: String, writer: BufferedWriter) {
        val returnCppType = getCppType(function.type)
        val packagePart = packageName.replace('.', '_')
        val camelCaseName = "${function.name[0].toLowerCase()}${function.name.substring(1)}"
        writer.write("""
            |JNIEXPORT $returnCppType JNICALL Java_${packagePart}_$camelCaseName
            |        (JNIEnv *env, jclass clazz""".trimMargin())
        if (function.parameters.isNotEmpty()) {
            writer.write(", ")
        }
        val parameters = function.parameters.joinToString(separator = ", ") {
            val parameterJavaType = when {
                it.hasAttribute("out") -> getCppOutType(it.type)
                else -> getCppType(it.type)
            }
            "$parameterJavaType ${it.name}"
        }
        writer.write(parameters)
        writer.write(") {\n")

        val methodParameterGenerators: List<MethodParameterGenerator> = getMethodParameterGenerators(function)

        methodParameterGenerators.mapNotNull { it.generatePreCallSetup() }.forEach { writer.write(it) }

        val resultProcessingSteps = methodParameterGenerators.asReversed().mapNotNull { it.generateResultProcessing() }

        when {
            resultProcessingSteps.isEmpty() && function.type != Types.VOID -> writer.write("    return ")
            function.type != Types.VOID -> writer.write("    auto _result = ")
        }
        writer.write("${function.name}(")
        writer.write(methodParameterGenerators.joinToString(separator = ", ") { it.generateMethodCallParameter() })
        writer.write(");\n")

        resultProcessingSteps.forEach { writer.write(it) }

        if (resultProcessingSteps.isNotEmpty() && function.type != Types.VOID) {
            writer.write("    return result;\n")
        }

        writer.write("}\n\n")
    }

    private fun getMethodParameterGenerators(function: Function): List<MethodParameterGenerator> {
        val methodParameterGenerators: MutableList<MethodParameterGenerator> = mutableListOf()
        var skipNextParameter = false
        val indentation = "    "

        for ((index, parameter) in function.parameters.withIndex()) {
            if (skipNextParameter) {
                skipNextParameter = false
                continue
            }

            methodParameterGenerators += when {
                parameter.type == Types.FLOAT && parameter.hasAttribute("out") ->
                    ReferenceFloatMethodParameterGenerator(
                            parameterName = parameter.name,
                            indentation = indentation
                    )
                parameter.type == Types.INT && parameter.hasAttribute("out") ->
                    ReferenceIntMethodParameterGenerator(
                            parameterName = parameter.name,
                            indentation = indentation
                    )
                parameter.type == Types.STRING && parameter.hasAttribute("out") -> {
                    skipNextParameter = true
                    val sizeParameterName = function.parameters[index + 1].name
                    ReferenceStringMethodParameterGenerator(
                            parameterName = parameter.name,
                            sizeParameterName = sizeParameterName,
                            indentation = indentation
                    )
                }
                parameter.type == Types.STRING ->
                    StringMethodParameterGenerator(
                            parameterName = parameter.name,
                            indentation = indentation
                    )
                else -> PrimitiveMethodParameterGenerator(parameter.name)
            }
        }
        return methodParameterGenerators
    }

    private fun writeFooter(writer: BufferedWriter) {
        writer.close()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val codeGeneratorArguments = CodeGeneratorArguments.parse(args)
            val interfaceDefinitionParser = InterfaceDefinitionParser()
            val functions = interfaceDefinitionParser.parse(*codeGeneratorArguments.interfaceDefinitionSources).functions
            SAMPNativeFunctionsCppCodeGenerator().generate(
                    outputDirectory = codeGeneratorArguments.outputDirectoryPath,
                    functions = functions,
                    packageName = codeGeneratorArguments.packageName
            )
        }
    }

}