package ch.leadrian.samp.kamp.examples.viewtest

import ch.leadrian.samp.kamp.core.api.inject.KampModule

class ViewTestModule : KampModule() {

    override fun configure() {
        bind(PlayerSpawner::class.java).asEagerSingleton()
        bind(PlayerClassSelector::class.java).asEagerSingleton()
        newCommandsSetBinder().apply {
            addBinding().to(ViewTestCommands::class.java)
        }
    }
}