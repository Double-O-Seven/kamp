package ch.leadrian.samp.kamp.examples.streamertest

import ch.leadrian.samp.kamp.core.api.inject.KampModule

class StreamerTestModule : KampModule() {

    override fun configure() {
        bind(PlayerSpawner::class.java).asEagerSingleton()
        bind(PlayerClassSelector::class.java).asEagerSingleton()
        bind(MapLoader::class.java).asEagerSingleton()
        newCommandsSetBinder().apply {
            addBinding().to(StreamerCommands::class.java)
        }
    }
}