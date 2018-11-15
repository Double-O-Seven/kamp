package ch.leadrian.samp.kamp.codegen

import java.io.File

internal interface CodeGenerator {

    val outputFiles: List<File>

    fun generate()

}