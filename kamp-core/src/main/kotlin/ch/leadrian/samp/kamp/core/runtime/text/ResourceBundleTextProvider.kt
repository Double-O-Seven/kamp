package ch.leadrian.samp.kamp.core.runtime.text

import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import java.util.*

internal class ResourceBundleTextProvider(private val resourceBundlePackages: Set<String>) : TextProvider {

    companion object {

        const val PROPERTIES_FILE_NAME = "strings"
    }

    private val resourceBundlesByLocale = mutableMapOf<Locale, ResourceBundleGroup>()

    override fun getText(locale: Locale, key: TextKey, defaultText: String?): String =
            resourceBundlesByLocale
                    .computeIfAbsent(locale) { createResourceBundleGroup(it) }
                    .getText(key)
                    ?: defaultText
                    ?: key.name

    private fun createResourceBundleGroup(locale: Locale): ResourceBundleGroup {
        val resourceBundles = resourceBundlePackages.map {
            ResourceBundle.getBundle("$it.$PROPERTIES_FILE_NAME", locale)
        }
        return ResourceBundleGroup(resourceBundles)
    }

    private class ResourceBundleGroup(private val resourceBundles: List<ResourceBundle>) {

        fun getText(key: TextKey): String? =
                resourceBundles
                        .filter { it.containsKey(key.name) }
                        .mapNotNull {
                            try {
                                it.getString(key.name)
                            } catch (e: MissingResourceException) {
                                null
                            }
                        }
                        .firstOrNull()
    }

}