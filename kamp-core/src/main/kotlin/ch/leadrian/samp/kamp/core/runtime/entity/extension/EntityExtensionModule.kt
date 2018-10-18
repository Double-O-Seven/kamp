package ch.leadrian.samp.kamp.core.runtime.entity.extension

import ch.leadrian.samp.kamp.core.api.inject.KampModule

class EntityExtensionModule : KampModule() {

    override fun configure() {
        newPlayerExtensionFactorySetBinder() // required in order to have at least an empty binding
    }
}