package ch.leadrian.samp.kamp.core.runtime.entity.registry

import com.google.inject.AbstractModule

internal class EntityRegistryModule : AbstractModule() {

    override fun configure() {
        bind(PlayerRegistry::class.java).asEagerSingleton()
        bind(PlayerSearchIndex::class.java).asEagerSingleton()
    }
}