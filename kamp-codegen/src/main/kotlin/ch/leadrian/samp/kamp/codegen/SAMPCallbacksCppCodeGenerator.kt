package ch.leadrian.samp.kamp.codegen

import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.cidl.model.Parameter
import ch.leadrian.samp.cidl.model.Types
import ch.leadrian.samp.cidl.parser.InterfaceDefinitionParser
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class SAMPCallbacksCppCodeGenerator {

    fun generate(outputDirectory: Path, functions: List<Function>, fileName: String = "SAMPCallbacks") {
        Files.createDirectories(outputDirectory)
        val outputFile = outputDirectory.resolve("$fileName.cpp")

        Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use { writer ->
            writeHeader(writer)
            writeFunctions(functions, writer)
            writeFooter(writer)
        }
    }

    private fun writeHeader(writer: BufferedWriter) {
        writer.write("""
            |
            |/* Do not edit this file, it is generated */
            |
            |#include <jni.h>
            |#include <sampgdk/core.h>
            |#include <sampgdk/sdk.h>
            |
            |#include "Kamp.hpp"
            |#include "SAMPCallbacksMethodCache.hpp"
            |
            |PLUGIN_EXPORT unsigned int PLUGIN_CALL Supports() {
            |    return sampgdk::Supports() | SUPPORTS_PROCESS_TICK;
            |}
            |
            |PLUGIN_EXPORT bool PLUGIN_CALL Load(void **ppData) {
            |    return sampgdk::Load(ppData);
            |}
            |
            |PLUGIN_EXPORT void PLUGIN_CALL Unload() {
            |    sampgdk::Unload();
            |}
            |
            |PLUGIN_EXPORT void PLUGIN_CALL ProcessTick() {
            |    sampgdk::ProcessTick();
            |    Kamp& kampInstance = Kamp::GetInstance();
            |    if (!kampInstance.IsLaunched()) {
            |        return;
            |    }
            |    jobject sampCallbacksInstance = kampInstance.GetSAMPCallbacksInstance();
            |    jmethodID callbackMethodID = kampInstance.GetSAMPCallbacksMethodCache().GetOnProcessTickMethodID();
            |    JNIEnv *jniEnv = kampInstance.GetJNIEnv();
            |    jniEnv->CallVoidMethod(sampCallbacksInstance, callbackMethodID);
            |}
            |
            |""".trimMargin("|"))
    }

    private fun writeFunctions(functions: List<Function>, writer: BufferedWriter) {
        functions
                .filter { it.hasAttribute("callback") }
                .forEach { writeFunction(it, writer) }
    }

    private fun writeFunction(function: Function, writer: BufferedWriter) {
        val returnType = getCppType(function.type)
        writer.write("PLUGIN_EXPORT $returnType PLUGIN_CALL ${function.name}(")
        function
                .parameters
                .joinToString(separator = ", ") {
                    val parameterType = getCppType(it.type)
                    "$parameterType ${it.name}"
                }
                .let { writer.write(it) }
        writer.write("""
            |) {
            |    Kamp& kampInstance = Kamp::GetInstance();
            |""".trimMargin())

        if (function.name == "OnGameModeInit") {
            writer.write("    kampInstance.Launch();\n")
        }

        writer.write("""
            |    jobject sampCallbacksInstance = kampInstance.GetSAMPCallbacksInstance();
            |    jmethodID callbackMethodID = kampInstance.GetSAMPCallbacksMethodCache().Get${function.name}MethodID();
            |    JNIEnv *jniEnv = kampInstance.GetJNIEnv();
            |""".trimMargin())

        val methodParameterGenerators = getMethodParameterGenerators(function.parameters)
        val resultProcessingSteps = methodParameterGenerators.mapNotNull { it.generateResultProcessing() }

        methodParameterGenerators.mapNotNull { it.generatePreCallSetup() }.forEach { writer.write("$it\n") }

        when {
            function.type == Types.BOOL -> writer.write("    jboolean _result = jniEnv->CallBooleanMethod(")
            function.type == Types.VOID -> writer.write("    jniEnv->CallVoidMethod(")
            else -> throw UnsupportedOperationException("Generating callback with return type ${function.type} is not supported")
        }
        writer.write("sampCallbacksInstance, callbackMethodID")
        if (methodParameterGenerators.isNotEmpty()) {
            writer.write(", ")
        }
        methodParameterGenerators.joinToString(separator = ", ") { it.generateMethodCallParameter() }.let { writer.write(it) }
        writer.write(");\n")

        if (resultProcessingSteps.isNotEmpty()) {
            resultProcessingSteps.forEach { writer.write("$it\n") }
        }

        if (function.name == "OnGameModeExit") {
            writer.write("    kampInstance.Shutdown();\n")
        }

        if (function.type == Types.BOOL) {
            writer.write("    return (_result ? true : false);\n")
        }
        writer.write("}\n\n")
    }

    private fun getMethodParameterGenerators(parameters: List<Parameter>): List<MethodParameterGenerator> =
            parameters.map {
                when (it.type) {
                    Types.STRING -> JstringMethodParameterGenerator(it.name, "    ")
                    else -> DefaultMethodParameterGenerator(it.name)
                }
            }

    private fun writeFooter(writer: BufferedWriter) {
        writer.close()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val codeGeneratorArguments = CodeGeneratorArguments.parse(args = args, packageNameRequired = false)
            val interfaceDefinitionParser = InterfaceDefinitionParser()
            val functions = interfaceDefinitionParser.parse(*codeGeneratorArguments.interfaceDefinitionSources).functions
            SAMPCallbacksCppCodeGenerator().generate(
                    outputDirectory = codeGeneratorArguments.outputDirectoryPath,
                    functions = functions
            )
        }
    }

}