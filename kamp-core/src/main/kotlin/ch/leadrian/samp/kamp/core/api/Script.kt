package ch.leadrian.samp.kamp.core.api

import com.google.inject.Module
import java.nio.file.Path

interface Script {

    fun getTextProviderResourcePackages(): Set<String>

    fun getInjectorBasePackages(): Set<String>

    fun getModules(): List<Module>

    val dataDirectory: Path

}