package ch.leadrian.samp.kamp.core.runtime

import ch.leadrian.samp.kamp.core.api.util.loggerFor
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path

internal object ClassPathExtender {

    private val log = loggerFor<ClassPathExtender>()

    @JvmStatic
    fun extendClassPath(jarsDirectory: Path) {
        try {
            val systemClassLoader = ClassLoader.getSystemClassLoader()
            if (systemClassLoader is URLClassLoader) {
                extendClassPath(jarsDirectory, systemClassLoader)
            } else {
                log.warn("Could not extend classpath, system class loader is not a ${URLClassLoader::class.qualifiedName}, all dependencies need to be specified explicitly in jvmopts.txt")
            }
        } catch (e: Exception) {
            log.error("Failed to extend classpath", e)
        }
    }

    private fun extendClassPath(jarsDirectory: Path, systemClassLoader: URLClassLoader) {
        val addURL = URLClassLoader::class.java.getDeclaredMethod("addURL", URL::class.java)
        addURL.isAccessible = true
        try {
            Files
                    .list(jarsDirectory)
                    .filter { Files.isRegularFile(it) }
                    .filter { it.fileName.toString().endsWith(".jar", ignoreCase = true) }
                    .forEach { addURL(systemClassLoader, it.toUri().toURL()) }
        } finally {
            addURL.isAccessible = false
        }
    }

}