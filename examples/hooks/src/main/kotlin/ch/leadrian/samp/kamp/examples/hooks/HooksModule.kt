package ch.leadrian.samp.kamp.examples.hooks

import ch.leadrian.samp.kamp.core.api.inject.KampModule

class HooksModule : KampModule() {

    override fun configure() {
        bind(PlayerSpawner::class.java).asEagerSingleton()
        bind(PlayerClassSelector::class.java).asEagerSingleton()
        newCommandsSetBinder().apply {
            addBinding().to(Commands::class.java)
        }
        newSAMPNativeFunctionHookFactorySetBinder().addBinding().to(ClientMessageLoggerFactory::class.java)
    }
}