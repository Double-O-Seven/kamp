package ch.leadrian.samp.kamp.codegen

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.Writer

internal abstract class SingleFileCodeGenerator(private val outputDirectory: File) : CodeGenerator {

    protected abstract val fileName: String

    private val outputFile: File by lazy {
        outputDirectory.resolve(fileName)
    }

    override val outputFiles: List<File>
        get() = listOf(outputFile)

    override fun generate() {
        outputDirectory.mkdirs()
        BufferedWriter(FileWriter(outputFile, false)).use(this::generate)
    }

    protected abstract fun generate(writer: Writer)

}