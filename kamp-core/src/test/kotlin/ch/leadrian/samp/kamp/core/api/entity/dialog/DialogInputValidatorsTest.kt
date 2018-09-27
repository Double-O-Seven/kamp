package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.TextKeys
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class DialogInputValidatorsTest {

    private val player = mockk<Player>()

    @Nested
    inner class NotBlankTests {

        @ParameterizedTest
        @ValueSource(strings = ["a", "123", "xyz"])
        fun givenValidValueItShouldReturnNull(inputText: String) {
            val validator = DialogInputValidators.notBlank()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isNull()
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " ", "   ", "\t", "\n"])
        fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult(inputText: String) {
            val validator = DialogInputValidators.notBlank()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo(TextKeys.dialog.input.validation.error.notblank)
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " ", "   ", "\t", "\n"])
        fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage(inputText: String) {
            val validator = DialogInputValidators.notBlank(errorMessage = "not blank plz")

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo("not blank plz")
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " ", "   ", "\t", "\n"])
        fun givenInvalidValueAndErrorMessageTextKeyItShouldReturnErrorMessageTextKey(inputText: String) {
            val errorMessageTextKey = TextKey("not.blank.plz")
            val validator = DialogInputValidators.notBlank(errorMessageTextKey = errorMessageTextKey)

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo(errorMessageTextKey)
        }

    }

    @Nested
    inner class NotEmptyTests {

        @ParameterizedTest
        @ValueSource(strings = ["a", "123", "xyz", " ", "   ", "\t", "\n"])
        fun givenValidValueItShouldReturnNull(inputText: String) {
            val validator = DialogInputValidators.notEmpty()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isNull()
        }

        @Test
        fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult() {
            val validator = DialogInputValidators.notEmpty()

            val result = validator.validate(player, "")

            assertThat(result)
                    .isEqualTo(TextKeys.dialog.input.validation.error.notempty)
        }

        @Test
        fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage() {
            val validator = DialogInputValidators.notEmpty(errorMessage = "not empty plz")

            val result = validator.validate(player, "")

            assertThat(result)
                    .isEqualTo("not empty plz")
        }

        @Test
        fun givenInvalidValueAndErrorMessageTextKeyItShouldReturnErrorMessageTextKey() {
            val errorMessageTextKey = TextKey("not.empty.plz")
            val validator = DialogInputValidators.notEmpty(errorMessageTextKey = errorMessageTextKey)

            val result = validator.validate(player, "")

            assertThat(result)
                    .isEqualTo(errorMessageTextKey)
        }

    }

    @Nested
    inner class PatternTests {

        private val regex = "\\d{2}\\.\\d{2}\\.\\d{4}"

        @ParameterizedTest
        @ValueSource(strings = ["00.00.0000", "04.02.1992", "31.12.2018"])
        fun givenValidValueItShouldReturnNull(inputText: String) {
            val validator = DialogInputValidators.pattern(regex)

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isNull()
        }

        @ParameterizedTest
        @ValueSource(strings = ["a", "123", "xyz", "04.02"])
        fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult(inputText: String) {
            val validator = DialogInputValidators.pattern(regex)

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo(TextKeys.dialog.input.validation.error.generic)
        }

        @ParameterizedTest
        @ValueSource(strings = ["a", "123", "xyz", "04.02"])
        fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage(inputText: String) {
            val validator = DialogInputValidators.pattern(regex = regex, errorMessage = "date plz")

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo("date plz")
        }

        @ParameterizedTest
        @ValueSource(strings = ["a", "123", "xyz", "04.02"])
        fun givenInvalidValueAndErrorMessageTextKeyItShouldReturnErrorMessageTextKey(inputText: String) {
            val errorMessageTextKey = TextKey("date.plz")
            val validator = DialogInputValidators.pattern(regex = regex, errorMessageTextKey = errorMessageTextKey)

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo(errorMessageTextKey)
        }

    }

    @Nested
    inner class FloatValueTests {

        @ParameterizedTest
        @ValueSource(strings = ["11", "0.123", "13.37", ".77", "7.", "-10", "-.1", "-6.9"])
        fun givenValidValueItShouldReturnNull(inputText: String) {
            val validator = DialogInputValidators.floatValue()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isNull()
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "abc", "???", "."])
        fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult(inputText: String) {
            val validator = DialogInputValidators.floatValue()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo(TextKeys.dialog.input.validation.error.floatvalue)
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "abc", "???", "."])
        fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage(inputText: String) {
            val validator = DialogInputValidators.floatValue(errorMessage = "float value plz")

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo("float value plz")
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "abc", "???", "."])
        fun givenInvalidValueAndErrorMessageTextKeyItShouldReturnErrorMessageTextKey(inputText: String) {
            val errorMessageTextKey = TextKey("float.value.plz")
            val validator = DialogInputValidators.floatValue(errorMessageTextKey = errorMessageTextKey)

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo(errorMessageTextKey)
        }

    }

    @Nested
    inner class IntValueTests {

        @ParameterizedTest
        @ValueSource(strings = ["11", "123", "0", "-10"])
        fun givenValidValueItShouldReturnNull(inputText: String) {
            val validator = DialogInputValidators.intValue()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isNull()
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "abc", "???", ".", "0.123", "13.37", ".77", "7."])
        fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult(inputText: String) {
            val validator = DialogInputValidators.intValue()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo(TextKeys.dialog.input.validation.error.intvalue)
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "abc", "???", ".", "0.123", "13.37", ".77", "7."])
        fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage(inputText: String) {
            val validator = DialogInputValidators.intValue(errorMessage = "int value plz")

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo("int value plz")
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "abc", "???", ".", "0.123", "13.37", ".77", "7."])
        fun givenInvalidValueAndErrorMessageTextKeyItShouldReturnErrorMessageTextKey(inputText: String) {
            val errorMessageTextKey = TextKey("int.value.plz")
            val validator = DialogInputValidators.intValue(errorMessageTextKey = errorMessageTextKey)

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo(errorMessageTextKey)
        }

    }

    @Nested
    inner class ContainedInTests {

        @Nested
        inner class CaseSensitiveTests {

            @ParameterizedTest
            @ValueSource(strings = ["YES", "NO"])
            fun givenValidValueItShouldReturnNull(inputText: String) {
                val validator = DialogInputValidators.containedIn("YES", "NO", ignoreCase = false)

                val result = validator.validate(player, inputText)

                assertThat(result)
                        .isNull()
            }

            @ParameterizedTest
            @ValueSource(strings = ["Yes", "No", "yes", "no", "hahaha", "", "lol"])
            fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult(inputText: String) {
                val validator = DialogInputValidators.containedIn("YES", "NO", ignoreCase = false)

                val result = validator.validate(player, inputText)

                assertThat(result)
                        .isEqualTo(TextKeys.dialog.input.validation.error.generic)
            }

            @ParameterizedTest
            @ValueSource(strings = ["Yes", "No", "yes", "no", "hahaha", "", "lol"])
            fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage(inputText: String) {
                val validator = DialogInputValidators.containedIn(
                        "YES",
                        "NO",
                        ignoreCase = false,
                        errorMessage = "yes or no plz"
                )

                val result = validator.validate(player, inputText)

                assertThat(result)
                        .isEqualTo("yes or no plz")
            }

            @ParameterizedTest
            @ValueSource(strings = ["Yes", "No", "yes", "no", "hahaha", "", "lol"])
            fun givenInvalidValueAndErrorMessageTextKeyItShouldReturnErrorMessageTextKey(inputText: String) {
                val errorMessageTextKey = TextKey("yes.or.no.plz")
                val validator = DialogInputValidators.containedIn(
                        "YES",
                        "NO",
                        ignoreCase = false,
                        errorMessageTextKey = errorMessageTextKey
                )

                val result = validator.validate(player, inputText)

                assertThat(result)
                        .isEqualTo(errorMessageTextKey)
            }
        }

        @Nested
        inner class CaseInsensitiveTests {

            @ParameterizedTest
            @ValueSource(strings = ["YES", "NO", "Yes", "No", "yes", "no"])
            fun givenValidValueItShouldReturnNull(inputText: String) {
                val validator = DialogInputValidators.containedIn("YES", "NO", ignoreCase = true)

                val result = validator.validate(player, inputText)

                assertThat(result)
                        .isNull()
            }

            @ParameterizedTest
            @ValueSource(strings = ["hahaha", "", "lol"])
            fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult(inputText: String) {
                val validator = DialogInputValidators.containedIn("YES", "NO", ignoreCase = true)

                val result = validator.validate(player, inputText)

                assertThat(result)
                        .isEqualTo(TextKeys.dialog.input.validation.error.generic)
            }

            @ParameterizedTest
            @ValueSource(strings = ["hahaha", "", "lol"])
            fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage(inputText: String) {
                val validator = DialogInputValidators.containedIn(
                        "YES",
                        "NO",
                        ignoreCase = true,
                        errorMessage = "yes or no plz"
                )

                val result = validator.validate(player, inputText)

                assertThat(result)
                        .isEqualTo("yes or no plz")
            }

            @ParameterizedTest
            @ValueSource(strings = ["hahaha", "", "lol"])
            fun givenInvalidValueAndErrorMessageTextKeyItShouldReturnErrorMessageTextKey(inputText: String) {
                val errorMessageTextKey = TextKey("yes.or.no.plz")
                val validator = DialogInputValidators.containedIn(
                        "YES",
                        "NO",
                        ignoreCase = true,
                        errorMessageTextKey = errorMessageTextKey
                )

                val result = validator.validate(player, inputText)

                assertThat(result)
                        .isEqualTo(errorMessageTextKey)
            }
        }

    }

}