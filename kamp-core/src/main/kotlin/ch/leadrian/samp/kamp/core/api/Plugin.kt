package ch.leadrian.samp.kamp.core.api

import java.nio.file.Path

abstract class Plugin : Script {

    /**
     * By default, the package of this instance will be used as package for string properties.
     * @see [Script.getTextProviderResourcePackages]
     */
    override fun getTextProviderResourcePackages(): Set<String> =
            setOf(this::class.java.getPackage().name)

    override fun getInjectorBasePackages(): Set<String> = emptySet()

    /**
     * During server start-up, the data directory for this [Plugin] will be derived from its package.
     * The directory will automatically be created if it doesn't exist yet.
     */
    final override lateinit var dataDirectory: Path
        internal set

}