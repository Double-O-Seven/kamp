package ch.leadrian.samp.kamp.codegen.cpp

import ch.leadrian.samp.cidl.model.Function
import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import ch.leadrian.samp.kamp.codegen.camelCaseName
import java.io.File
import java.io.Writer

internal class SAMPCallbacksMethodCacheCppGenerator(
        private val functions: List<Function>,
        outputDirectory: File
) : SingleFileCodeGenerator(outputDirectory) {

    override val fileName: String = "SAMPCallbacksMethodCache.cpp"

    override fun generate(writer: Writer) {
        writer.writeHeader()
        writer.writeMethodIDInitializations(functions)
        writer.writeFooter()
    }

    private fun Writer.writeHeader() {
        write(
                """
            |
            |/* Do not edit this file, it is generated */
            |
            |#include <jni.h>
            |
            |#include "SAMPCallbacksMethodCache.hpp"
            |
            |int SAMPCallbacksMethodCache::Initialize(JNIEnv *jniEnv, jclass clazz) {
            |    this->onProcessTickMethodID = jniEnv->GetMethodID(clazz, "onProcessTick", "()V");
            |    if (this->onProcessTickMethodID == nullptr) {
            |        return -1;
            |    }
            |
            |
        """.trimMargin("|")
        )
    }

    private fun Writer.writeMethodIDInitializations(functions: List<Function>) {
        functions
                .filter { it.hasAttribute("callback") }
                .forEachIndexed { index, function ->
                    val errorValue = -(index + 2)
                    writeMethodIDInitialization(function, errorValue)
                }
    }

    private fun Writer.writeMethodIDInitialization(function: Function, errorValue: Int) {
        val camelCaseName = function.camelCaseName
        val methodSignature = getCallbackMethodSignature(function)
        write(
                """
            |    this->${camelCaseName}MethodID = jniEnv->GetMethodID(clazz, "$camelCaseName", "$methodSignature");
            |    if (this->${camelCaseName}MethodID == nullptr) {
            |        return $errorValue;
            |    }
            |
            |
        """.trimMargin()
        )
    }

    private fun getCallbackMethodSignature(function: Function): String {
        val parameterSignature = function.parameters.joinToString(separator = "") { getJvmTypeSignature(it.type) }
        return "(" + parameterSignature + ")" + getJvmTypeSignature(function.type)
    }

    private fun Writer.writeFooter() {
        write("    return 0;\n}\n")
    }

}