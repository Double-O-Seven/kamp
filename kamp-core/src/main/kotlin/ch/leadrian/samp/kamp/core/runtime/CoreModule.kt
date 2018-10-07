package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.GameMode
import ch.leadrian.samp.kamp.core.api.Plugin
import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.core.runtime.async.AsyncModule
import ch.leadrian.samp.kamp.core.runtime.callback.CallbackModule
import ch.leadrian.samp.kamp.core.runtime.command.CommandModule
import ch.leadrian.samp.kamp.core.runtime.entity.factory.EntityFactoryModule
import ch.leadrian.samp.kamp.core.runtime.entity.registry.EntityRegistryModule
import ch.leadrian.samp.kamp.core.runtime.service.ServiceModule
import ch.leadrian.samp.kamp.core.runtime.text.TextModule
import ch.leadrian.samp.kamp.core.runtime.timer.TimerModule

internal class CoreModule(
        private val server: Server,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textProviderResourcePackages: Set<String>,
        private val gameMode: GameMode,
        private val plugins: List<Plugin>
) : KampModule() {

    override fun configure() {
        install(AsyncModule())
        install(CallbackModule())
        install(CommandModule())
        install(EntityFactoryModule())
        install(EntityRegistryModule())
        install(ServiceModule())
        install(TextModule())
        install(TimerModule())

        bind(Server::class.java).toInstance(server)
        bind(SAMPNativeFunctionExecutor::class.java).toInstance(nativeFunctionExecutor)
        bind(GameMode::class.java).toInstance(gameMode)
        bind(gameMode.javaClass).toInstance(gameMode)
        plugins.forEach { plugin ->
            bind(plugin.javaClass).toInstance(plugin)
        }

        newTextProviderResourceBundlePackagesSetBinder().apply {
            textProviderResourcePackages.forEach {
                addBinding().toInstance(it)
            }
        }

    }
}