package ch.leadrian.samp.kamp.examples.lvdm

import ch.leadrian.samp.kamp.core.api.inject.KampModule

class LvdmModule : KampModule() {

    override fun configure() {
        bind(PlayerSpawner::class.java).asEagerSingleton()
        bind(PlayerClassSelector::class.java).asEagerSingleton()
        bind(PlayerDeathHandler::class.java).asEagerSingleton()
        newCommandsSetBinder().apply {
            addBinding().to(LvdmCommands::class.java)
            addBinding().to(AdminCommands::class.java)
            addBinding().to(DebugCommands::class.java)
        }
    }
}