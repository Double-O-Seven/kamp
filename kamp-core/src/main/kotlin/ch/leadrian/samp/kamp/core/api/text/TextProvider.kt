package ch.leadrian.samp.kamp.core.api.text

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

        internal const val RESOURCE_BUNDLE_PACKAGES_NAME = "textProviderResourceBundlePackages"

        const val PROPERTIES_FILE_NAME = "strings"

    }

    private val resourceBundlesByLocale = mutableMapOf<Locale, ResourceBundleGroup>()

    fun getText(locale: Locale, key: TextKey, defaultText: String? = null): String =
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