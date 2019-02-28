package ch.leadrian.samp.kamp.examples.amxinteroptest

import ch.leadrian.samp.kamp.core.api.inject.KampModule

class AmxInteropTestModule : KampModule() {

    override fun configure() {
        bind(PlayerSpawner::class.java).asEagerSingleton()
        bind(PlayerClassSelector::class.java).asEagerSingleton()
        bind(AmxCallbacks::class.java).asEagerSingleton()
        newCommandsSetBinder().apply {
            addBinding().to(Commands::class.java)
        }
    }
}