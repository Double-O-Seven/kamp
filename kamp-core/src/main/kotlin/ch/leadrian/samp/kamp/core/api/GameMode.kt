package ch.leadrian.samp.kamp.core.api

import java.nio.file.Path

abstract class GameMode : Script {

    override fun getTextProviderResourcePackages(): Set<String> =
            setOf(this::class.java.getPackage().name)

    override fun getInjectorBasePackages(): Set<String> = emptySet()

    override lateinit var dataDirectory: Path
        internal set

    abstract fun getPlugins(): List<Plugin>

}