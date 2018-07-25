package ch.leadrian.samp.kamp.apicodegen

import java.io.InputStream

interface InterfaceDefinitionSource {

    fun getInputStream(): InputStream

}