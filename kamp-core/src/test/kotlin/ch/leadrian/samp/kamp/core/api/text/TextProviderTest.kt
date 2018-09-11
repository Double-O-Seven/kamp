package ch.leadrian.samp.kamp.core.api.text

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.*
import java.util.stream.Stream

internal class TextProviderTest {

    private val defaultLocale = Locale.getDefault()

    @BeforeEach
    fun setUp() {
        Locale.setDefault(Locale.ROOT)
    }

    @AfterEach
    fun tearDown() {
        Locale.setDefault(defaultLocale)
    }

    @ParameterizedTest
    @ArgumentsSource(GetTextArgumentsProvider::class)
    fun shouldGetText(
            resourceBundlePackages: Set<String>,
            locale: Locale,
            textKey: TextKey,
            expectedResult: String,
            defaultText: String?
    ) {
        val textProvider = TextProvider(resourceBundlePackages)

        val result = textProvider.getText(locale, textKey, defaultText)

        assertThat(result)
                .isEqualTo(expectedResult)
    }

    private class GetTextArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<GetTextArguments> =
                Stream.of(
                        GetTextArguments(
                                resourceBundlePackages = setOf(getPackageName() + ".test1"),
                                locale = Locale.GERMANY,
                                textKey = "test.a",
                                expectedResult = "Hallo"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(getPackageName() + ".test1"),
                                locale = Locale.ENGLISH,
                                textKey = "test.a",
                                expectedResult = "Hello"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(getPackageName() + ".test1"),
                                locale = Locale.FRENCH,
                                textKey = "test.a",
                                expectedResult = "Hi there"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(getPackageName() + ".test1"),
                                locale = Locale.GERMANY,
                                textKey = "test.b",
                                expectedResult = "Test"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(getPackageName() + ".test1"),
                                locale = Locale.ENGLISH,
                                textKey = "test.b",
                                expectedResult = "Test"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(getPackageName() + ".test1"),
                                locale = Locale.FRENCH,
                                textKey = "test.b",
                                expectedResult = "Test"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.GERMANY,
                                textKey = "test.a",
                                expectedResult = "Hallo"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.ENGLISH,
                                textKey = "test.a",
                                expectedResult = "Hello"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.FRENCH,
                                textKey = "test.a",
                                expectedResult = "Hi there"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.GERMANY,
                                textKey = "test.b",
                                expectedResult = "Test"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.ENGLISH,
                                textKey = "test.b",
                                expectedResult = "Test"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.FRENCH,
                                textKey = "test.b",
                                expectedResult = "Test"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.GERMANY,
                                textKey = "test.c",
                                expectedResult = "DE C"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.ENGLISH,
                                textKey = "test.c",
                                expectedResult = "Default C"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.FRENCH,
                                textKey = "test.c",
                                expectedResult = "Default C"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.GERMANY,
                                textKey = "test.d",
                                expectedResult = "Default D"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.ENGLISH,
                                textKey = "test.d",
                                expectedResult = "EN D"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.FRENCH,
                                textKey = "test.d",
                                expectedResult = "Default D"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.FRENCH,
                                textKey = "test.x",
                                expectedResult = "test.x"
                        ),
                        GetTextArguments(
                                resourceBundlePackages = setOf(
                                        getPackageName() + ".test1",
                                        getPackageName() + ".test2"
                                ),
                                locale = Locale.FRENCH,
                                textKey = "test.x",
                                defaultText = "bla",
                                expectedResult = "bla"
                        )
                )

        fun getPackageName() = this::class.java.getPackage().name!!

    }

    private class GetTextArguments(
            private val resourceBundlePackages: Set<String>,
            private val locale: Locale,
            private val textKey: String,
            private val expectedResult: String,
            private val defaultText: String? = null
    ) : Arguments {

        override fun get(): Array<Any?> = arrayOf(resourceBundlePackages, locale, TextKey(textKey), expectedResult, defaultText)

    }
}