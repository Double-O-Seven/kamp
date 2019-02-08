package ch.leadrian.samp.kamp.examples.streamertest

import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.geodata.node.NodeFileSource
import ch.leadrian.samp.kamp.geodata.node.ResourcesNodeFileSource

class StreamerTestModule : KampModule() {

    override fun configure() {
        bind(PlayerSpawner::class.java).asEagerSingleton()
        bind(PlayerClassSelector::class.java).asEagerSingleton()
        bind(MapLoader::class.java).asEagerSingleton()
        bind(MapIconLoader::class.java).asEagerSingleton()
        bind(SanAndreasZoneLoader::class.java).asEagerSingleton()
        bind(NodeFileSource::class.java).toInstance(ResourcesNodeFileSource(StreamerTestGameMode::class.java))
        newCommandsSetBinder().apply {
            addBinding().to(StreamerCommands::class.java)
        }
    }
}