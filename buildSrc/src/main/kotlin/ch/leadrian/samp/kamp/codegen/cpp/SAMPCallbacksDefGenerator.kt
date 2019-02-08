package ch.leadrian.samp.kamp.codegen.cpp

import ch.leadrian.samp.kamp.cidl.model.Function
import ch.leadrian.samp.kamp.codegen.SingleFileCodeGenerator
import ch.leadrian.samp.kamp.codegen.isCallback
import java.io.File
import java.io.Writer

internal class SAMPCallbacksDefGenerator(
        private val functions: List<Function>,
        outputDirectory: File
) : SingleFileCodeGenerator(outputDirectory) {

    override val fileName: String = "SAMPCallbacks.def"

    override fun generate(writer: Writer) {
        writer.writeDefaultFunctions()
        writer.writeCallbacks()
    }

    private fun Writer.writeCallbacks() {
        functions
                .filter { it.isCallback }
                .forEach { write("    ${it.name}\n") }
    }

    private fun Writer.writeDefaultFunctions() {
        write("EXPORTS\n")
        write(
                """
            |    Supports
            |    Load
            |    Unload
            |    AmxLoad
            |    AmxUnload
            |    ProcessTick
            |
        """.trimMargin()
        )
    }
}