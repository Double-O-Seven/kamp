package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.GameMode
import ch.leadrian.samp.kamp.core.api.Plugin
import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.core.runtime.callback.CallbackProcessor
import ch.leadrian.samp.kamp.core.runtime.inject.InjectorFactory
import com.google.inject.Injector
import com.google.inject.Module
import com.google.inject.Stage
import com.netflix.governator.configuration.PropertiesConfigurationProvider
import com.netflix.governator.guice.BootstrapModule
import com.netflix.governator.lifecycle.LifecycleManager
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

internal class Server
private constructor(
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val configProperties: Properties,
        private val dataDirectory: Path,
        private val stage: Stage
) {

    companion object {

        const val GAME_MODE_CLASS_PROPERTY_KEY = "kamp.gamemode.class.name"

        @JvmOverloads
        @JvmStatic
        fun start(
                nativeFunctionExecutor: SAMPNativeFunctionExecutor,
                configProperties: Properties,
                dataDirectory: Path,
                stage: Stage = Stage.PRODUCTION
        ): Server {
            return Server(nativeFunctionExecutor, configProperties, dataDirectory, stage).apply {
                bootstrap()
                start()
            }
        }
    }

    private lateinit var gameMode: GameMode

    private lateinit var plugins: List<Plugin>

    lateinit var injector: Injector

    lateinit var callbackProcessor: CallbackProcessor
        private set

    private lateinit var lifecycleManager: LifecycleManager

    private fun bootstrap() {
        loadGameMode()
        loadPlugins()
        initializeNativeFunctionExecutor()
        initializeDataDirectories()
        createInjector()
    }

    private fun initializeDataDirectories() {
        gameMode.dataDirectory = dataDirectory.resolve(gameMode.javaClass.name).also {
            if (stage == Stage.PRODUCTION) {
                Files.createDirectories(it)
            }
        }
        plugins.forEach { plugin ->
            plugin.dataDirectory = dataDirectory.resolve(plugin.javaClass.name).also {
                if (stage == Stage.PRODUCTION) {
                    Files.createDirectories(it)
                }
            }
        }
    }

    private fun start() {
        callbackProcessor = injector.getInstance()
        lifecycleManager = injector.getInstance()
        lifecycleManager.start()
    }

    fun stop() {
        lifecycleManager.close()
    }

    private fun loadGameMode() {
        val gameModeClassName = configProperties.getProperty(GAME_MODE_CLASS_PROPERTY_KEY)
                ?: throw IllegalStateException("Could not find required property $GAME_MODE_CLASS_PROPERTY_KEY")
        val gameModeClass = Class.forName(gameModeClassName)
        gameMode = gameModeClass.newInstance() as GameMode
    }

    private fun loadPlugins() {
        plugins = gameMode.getPlugins().toList()
    }

    private fun initializeNativeFunctionExecutor() {
        nativeFunctionExecutor.initialize()
    }

    private fun createInjector() {
        val modules = mutableListOf<Module>()
        modules += getCoreModule()
        modules += gameMode.getModules()
        modules += plugins.flatMap { it.getModules() }
        injector = InjectorFactory.createInjector(
                basePackages = getInjectorBasePackages(),
                bootstrapModule = getBootstrapModule(),
                modules = *modules.toTypedArray(),
                stage = stage
        )
    }

    private fun getBootstrapModule(): BootstrapModule =
            BootstrapModule { binder ->
                binder.bindConfigurationProvider().toInstance(PropertiesConfigurationProvider(configProperties))
            }

    private fun getCoreModule(): Module =
            CoreModule(
                    server = this,
                    nativeFunctionExecutor = nativeFunctionExecutor,
                    textProviderResourcePackages = getTextProviderResourcePackages(),
                    gameMode = gameMode,
                    plugins = plugins
            )

    private fun getTextProviderResourcePackages(): Set<String> {
        val packages = mutableSetOf<String>()
        packages += gameMode.getTextProviderResourcePackages()
        plugins.forEach {
            packages += it.getTextProviderResourcePackages()
        }
        return packages
    }

    private fun getInjectorBasePackages(): Set<String> {
        val packages = mutableSetOf<String>()
        packages += gameMode.getInjectorBasePackages()
        plugins.forEach {
            packages += it.getInjectorBasePackages()
        }
        return packages
    }

}