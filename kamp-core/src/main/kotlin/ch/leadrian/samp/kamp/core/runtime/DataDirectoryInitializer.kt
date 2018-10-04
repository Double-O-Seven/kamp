package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.GameMode
import ch.leadrian.samp.kamp.core.api.Plugin
import java.nio.file.Files
import java.nio.file.Path
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DataDirectoryInitializer
@Inject
constructor(
        private val gameMode: GameMode,
        private val plugins: Set<@JvmSuppressWildcards Plugin>
) {

    @PostConstruct
    fun initialize() {
        createDirectoryIfNotExists(gameMode.dataDirectory)
        plugins.forEach { plugin ->
            createDirectoryIfNotExists(plugin.dataDirectory)
        }
    }

    private fun createDirectoryIfNotExists(path: Path) {
        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }
    }

}