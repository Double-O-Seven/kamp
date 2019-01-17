package ch.leadrian.samp.kamp.core.runtime.text

import ch.leadrian.samp.kamp.core.api.inject.KampModule

internal class TextModule : KampModule() {

    override fun configure() {
        newTextProviderResourceBundlePackagesSetBinder().apply {
            addBinding().toInstance("ch.leadrian.samp.kamp.core")
        }
    }
}