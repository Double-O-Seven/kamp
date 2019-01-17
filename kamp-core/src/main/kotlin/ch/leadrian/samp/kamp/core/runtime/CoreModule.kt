package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.GameMode
import ch.leadrian.samp.kamp.core.api.Plugin
import ch.leadrian.samp.kamp.core.api.inject.KampModule
import ch.leadrian.samp.kamp.core.runtime.callback.CallbackModule
import ch.leadrian.samp.kamp.core.runtime.command.CommandModule
import ch.leadrian.samp.kamp.core.runtime.entity.extension.EntityExtensionModule
import ch.leadrian.samp.kamp.core.runtime.entity.registry.EntityRegistryModule
import ch.leadrian.samp.kamp.core.runtime.text.TextModule
import com.google.inject.multibindings.Multibinder
import com.google.inject.name.Names.named

internal class CoreModule(
        private val server: Server,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val textProviderResourcePackages: Set<String>,
        private val gameMode: GameMode,
        private val plugins: List<Plugin>
) : KampModule() {

    override fun configure() {
        installModules()
        bindServer()
        bindNativeFunctionExecutor()
        bindGameMode()
        bindPlugins()
        bindTextProviderResourcePackages()
        newSAMPNativeFunctionHookFactorySetBinder()
    }

    private fun installModules() {
        install(CallbackModule())
        install(CommandModule())
        install(EntityExtensionModule())
        install(EntityRegistryModule())
        install(TextModule())
    }

    private fun bindServer() {
        bind(Server::class.java).toInstance(server)
    }

    private fun bindNativeFunctionExecutor() {
        bind(SAMPNativeFunctionExecutor::class.java)
                .annotatedWith(named(SAMPNativeFunctionExecutorProvider.BASE_NATIVE_FUNCTION_EXECUTOR_NAME))
                .toInstance(nativeFunctionExecutor)
        bind(SAMPNativeFunctionExecutor::class.java).toProvider(SAMPNativeFunctionExecutorProvider::class.java)
    }

    private fun bindGameMode() {
        bind(GameMode::class.java).toInstance(gameMode)
        bind(gameMode.javaClass).toInstance(gameMode)
    }

    private fun bindPlugins() {
        val pluginSetBinder = Multibinder.newSetBinder(binder(), Plugin::class.java)
        plugins.forEach { plugin ->
            bind(plugin.javaClass).toInstance(plugin)
            pluginSetBinder.addBinding().toInstance(plugin)
        }
    }

    private fun bindTextProviderResourcePackages() {
        newTextProviderResourceBundlePackagesSetBinder().apply {
            textProviderResourcePackages.forEach {
                addBinding().toInstance(it)
            }
        }
    }
}