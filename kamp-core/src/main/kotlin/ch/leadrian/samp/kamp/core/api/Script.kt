package ch.leadrian.samp.kamp.core.api

import com.google.inject.Module
import java.nio.file.Path

interface Script {

    /**
     * Returns a set of package names where the [ch.leadrian.samp.kamp.core.api.text.TextProvider]
     * will load string_<language>.properties files for translations.
     */
    fun getTextProviderResourcePackages(): Set<String>

    /**
     * Returns a set of base packages in which the injector will look for [com.netflix.governator.annotations.AutoBindSingleton]s.
     * Note that [com.netflix.governator.annotations.AutoBindSingleton] is deprecated.
     * @see [AutoBindSingleton](https://github.com/Netflix/governator/wiki/AutoBindSingleton)
     */
    fun getInjectorBasePackages(): Set<String>

    /**
     * Returns a list of Guice modules that will be used for dependencies injection.
     */
    fun getModules(): List<Module>

    val dataDirectory: Path

}