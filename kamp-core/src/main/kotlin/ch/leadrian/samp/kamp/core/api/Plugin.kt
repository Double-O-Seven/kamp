package ch.leadrian.samp.kamp.core.api

import com.google.inject.Module
import java.nio.file.Path

abstract class Plugin {

    fun getTextProviderResourcePackages(): Set<String> =
            setOf(this::class.java.getPackage().name)

    fun getInjectorBasePackages(): Set<String> = emptySet()

    abstract fun getModules(): List<Module>

    lateinit var dataDirectory: Path
        internal set

}