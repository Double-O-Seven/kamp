package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.GameMode
import ch.leadrian.samp.kamp.core.api.Plugin
import ch.leadrian.samp.kamp.core.api.util.getInstance
import ch.leadrian.samp.kamp.core.runtime.callback.CallbackProcessor
import ch.leadrian.samp.kamp.core.runtime.inject.InjectorFactory
import com.google.inject.Injector
import com.google.inject.Module
import com.netflix.governator.configuration.PropertiesConfigurationProvider
import com.netflix.governator.guice.BootstrapModule
import com.netflix.governator.lifecycle.LifecycleManager
import java.nio.charset.StandardCharsets.ISO_8859_1
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

internal class Server
private constructor(private val nativeFunctionExecutor: SAMPNativeFunctionExecutor) {

    companion object {

        val DATA_DIRECTORY: Path = Paths.get(".", "Kamp", "data")

        const val CONFIG_PROPERTIES_FILE = "config.properties"

        const val GAME_MODE_CLASS_PROPERTY_KEY = "kamp.gamemode.class.name"

        @JvmStatic
        fun start(nativeFunctionExecutor: SAMPNativeFunctionExecutor): Server {
            return Server(nativeFunctionExecutor).apply {
                bootstrap()
                start()
            }
        }
    }

    private lateinit var configProperties: Properties

    private lateinit var gameMode: GameMode

    private lateinit var plugins: List<Plugin>

    lateinit var injector: Injector

    lateinit var callbackProcessor: CallbackProcessor
        private set

    lateinit var lifecycleManager: LifecycleManager

    private fun bootstrap() {
        loadConfigProperties()
        loadGameMode()
        loadPlugins()
        initializeNativeFunctionExecutor()
        createInjector()
    }

    private fun start() {
        callbackProcessor = injector.getInstance()
        lifecycleManager = injector.getInstance()
        lifecycleManager.start()
    }

    fun stop() {
        lifecycleManager.close()
    }

    private fun loadConfigProperties() {
        Files.newBufferedReader(DATA_DIRECTORY.resolve(CONFIG_PROPERTIES_FILE), ISO_8859_1).use { reader ->
            configProperties = Properties().apply { load(reader) }
        }
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
                modules = *modules.toTypedArray()
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