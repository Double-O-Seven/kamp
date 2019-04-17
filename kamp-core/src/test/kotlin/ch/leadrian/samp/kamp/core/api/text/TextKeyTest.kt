package ch.leadrian.samp.kamp.core.api.text

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class TextKeyTest {

    @Test
    fun givenNoSegmentsResolveShouldReturnReceiver() {
        val textKey = TextKey("foo.bar")

        val resolvedTextKey = textKey.resolve()

        assertThat(resolvedTextKey)
                .isSameAs(textKey)
    }

    @Test
    fun shouldResolveSingleSegment() {
        val textKey = TextKey("foo.bar")

        val resolvedTextKey = textKey.resolve("bla")

        assertThat(resolvedTextKey)
                .isEqualTo(TextKey("foo.bar.bla"))
    }

    @Test
    fun shouldResolveMultipleSegments() {
        val textKey = TextKey("foo.bar")

        val resolvedTextKey = textKey.resolve("bla", "blub")

        assertThat(resolvedTextKey)
                .isEqualTo(TextKey("foo.bar.bla.blub"))
    }

    @ParameterizedTest
    @CsvSource(
            "BLA, false, false, haha.lol.BLA",
            "BLA, true, false, haha.lol.bla",
            "BLA, false, true, haha.lol.BLA",
            "BLA, true, true, haha.lol.bla",
            "FOO_BAR, false, false, haha.lol.FOO_BAR",
            "FOO_BAR, true, false, haha.lol.foo_bar",
            "FOO_BAR, false, true, haha.lol.FOOBAR",
            "FOO_BAR, true, true, haha.lol.foobar"
    )
    fun shouldResolveEnum(
            enumValue: TestEnum,
            toLowerCase: Boolean,
            removeUnderscores: Boolean,
            expectedTextKey: TextKey
    ) {
        val textKey = TextKey("haha.lol")

        val resolvedTextKey = textKey.resolve(
                value = enumValue,
                toLowerCase = toLowerCase,
                removeUnderscores = removeUnderscores
        )

        assertThat(resolvedTextKey)
                .isEqualTo(expectedTextKey)
    }

    internal enum class TestEnum {
        FOO_BAR,
        BLA
    }

}