package ch.leadrian.samp.kamp.core.api.text

import ch.leadrian.samp.kamp.core.api.util.loggerFor
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TextProvider
@Inject
internal constructor(
        @Named(RESOURCE_BUNDLE_PACKAGES_NAME)
        private val resourceBundlePackages: Set<@JvmSuppressWildcards String>
) {

    companion object {

        const val RESOURCE_BUNDLE_PACKAGES_NAME = "textProviderResourceBundlePackages"

        const val PROPERTIES_FILE_NAME = "strings"

        private val log = loggerFor<TextProvider>()

    }

    private val resourceBundlesByLocale = mutableMapOf<Locale, ResourceBundleGroup>()

    @JvmOverloads
    fun getText(locale: Locale, key: TextKey, defaultText: String? = null): String {
        val text = (resourceBundlesByLocale
                .computeIfAbsent(locale) { createResourceBundleGroup(it) }
                .getText(key)
                ?: defaultText)
        if (text == null) {
            log.warn("Could not find text for key ${key.name}")
        }
        return text ?: key.name
    }

    private fun createResourceBundleGroup(locale: Locale): ResourceBundleGroup {
        val resourceBundles = resourceBundlePackages.mapNotNull {
            try {
                ResourceBundle.getBundle("$it.$PROPERTIES_FILE_NAME", locale)
            } catch (e: MissingResourceException) {
                null
            }
        }
        return ResourceBundleGroup(resourceBundles)
    }

    private class ResourceBundleGroup(private val resourceBundles: List<ResourceBundle>) {

        fun getText(key: TextKey): String? =
                resourceBundles
                        .asSequence()
                        .filter { it.containsKey(key.name) }
                        .mapNotNull { it.getString(key.name) }
                        .firstOrNull()
    }

}