package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.KampCoreTextKeys
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogInputValidators.blankOr
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogInputValidators.containedIn
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogInputValidators.floatValue
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogInputValidators.intValue
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogInputValidators.notBlank
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogInputValidators.notEmpty
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogInputValidators.pattern
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
            val validator = notBlank()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isNull()
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " ", "   ", "\t", "\n"])
        fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult(inputText: String) {
            val validator = notBlank()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo(KampCoreTextKeys.dialog.input.validation.error.notblank)
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " ", "   ", "\t", "\n"])
        fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage(inputText: String) {
            val validator = notBlank(errorMessage = "not blank plz")

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo("not blank plz")
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " ", "   ", "\t", "\n"])
        fun givenInvalidValueAndErrorMessageTextKeyItShouldReturnErrorMessageTextKey(inputText: String) {
            val errorMessageTextKey = TextKey("not.blank.plz")
            val validator = notBlank(errorMessageTextKey = errorMessageTextKey)

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
            val validator = notEmpty()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isNull()
        }

        @Test
        fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult() {
            val validator = notEmpty()

            val result = validator.validate(player, "")

            assertThat(result)
                    .isEqualTo(KampCoreTextKeys.dialog.input.validation.error.notempty)
        }

        @Test
        fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage() {
            val validator = notEmpty(errorMessage = "not empty plz")

            val result = validator.validate(player, "")

            assertThat(result)
                    .isEqualTo("not empty plz")
        }

        @Test
        fun givenInvalidValueAndErrorMessageTextKeyItShouldReturnErrorMessageTextKey() {
            val errorMessageTextKey = TextKey("not.empty.plz")
            val validator = notEmpty(errorMessageTextKey = errorMessageTextKey)

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
            val validator = pattern(regex)

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isNull()
        }

        @ParameterizedTest
        @ValueSource(strings = ["a", "123", "xyz", "04.02"])
        fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult(inputText: String) {
            val validator = pattern(regex)

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo(KampCoreTextKeys.dialog.input.validation.error.generic)
        }

        @ParameterizedTest
        @ValueSource(strings = ["a", "123", "xyz", "04.02"])
        fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage(inputText: String) {
            val validator = pattern(regex = regex, errorMessage = "date plz")

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo("date plz")
        }

        @ParameterizedTest
        @ValueSource(strings = ["a", "123", "xyz", "04.02"])
        fun givenInvalidValueAndErrorMessageTextKeyItShouldReturnErrorMessageTextKey(inputText: String) {
            val errorMessageTextKey = TextKey("date.plz")
            val validator = pattern(regex = regex, errorMessageTextKey = errorMessageTextKey)

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
            val validator = floatValue()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isNull()
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "abc", "???", "."])
        fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult(inputText: String) {
            val validator = floatValue()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo(KampCoreTextKeys.dialog.input.validation.error.floatvalue)
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "abc", "???", "."])
        fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage(inputText: String) {
            val validator = floatValue(errorMessage = "float value plz")

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo("float value plz")
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "abc", "???", "."])
        fun givenInvalidValueAndErrorMessageTextKeyItShouldReturnErrorMessageTextKey(inputText: String) {
            val errorMessageTextKey = TextKey("float.value.plz")
            val validator = floatValue(errorMessageTextKey = errorMessageTextKey)

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
            val validator = intValue()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isNull()
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "abc", "???", ".", "0.123", "13.37", ".77", "7."])
        fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult(inputText: String) {
            val validator = intValue()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo(KampCoreTextKeys.dialog.input.validation.error.intvalue)
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "abc", "???", ".", "0.123", "13.37", ".77", "7."])
        fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage(inputText: String) {
            val validator = intValue(errorMessage = "int value plz")

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo("int value plz")
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "  ", "abc", "???", ".", "0.123", "13.37", ".77", "7."])
        fun givenInvalidValueAndErrorMessageTextKeyItShouldReturnErrorMessageTextKey(inputText: String) {
            val errorMessageTextKey = TextKey("int.value.plz")
            val validator = intValue(errorMessageTextKey = errorMessageTextKey)

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
                val validator = containedIn("YES", "NO", ignoreCase = false)

                val result = validator.validate(player, inputText)

                assertThat(result)
                        .isNull()
            }

            @ParameterizedTest
            @ValueSource(strings = ["Yes", "No", "yes", "no", "hahaha", "", "lol"])
            fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult(inputText: String) {
                val validator = containedIn("YES", "NO", ignoreCase = false)

                val result = validator.validate(player, inputText)

                assertThat(result)
                        .isEqualTo(KampCoreTextKeys.dialog.input.validation.error.generic)
            }

            @ParameterizedTest
            @ValueSource(strings = ["Yes", "No", "yes", "no", "hahaha", "", "lol"])
            fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage(inputText: String) {
                val validator = containedIn(
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
                val validator = containedIn(
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
                val validator = containedIn("YES", "NO", ignoreCase = true)

                val result = validator.validate(player, inputText)

                assertThat(result)
                        .isNull()
            }

            @ParameterizedTest
            @ValueSource(strings = ["hahaha", "", "lol"])
            fun givenInvalidValueAndNoErrorMessagesItShouldReturnDefaultResult(inputText: String) {
                val validator = containedIn("YES", "NO", ignoreCase = true)

                val result = validator.validate(player, inputText)

                assertThat(result)
                        .isEqualTo(KampCoreTextKeys.dialog.input.validation.error.generic)
            }

            @ParameterizedTest
            @ValueSource(strings = ["hahaha", "", "lol"])
            fun givenInvalidValueAndErrorMessageItShouldReturnErrorMessage(inputText: String) {
                val validator = containedIn(
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
                val validator = containedIn(
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

    @Nested
    inner class EmptyOrTests {

        @Test
        fun givenInputTextIsEmptyItShouldReturnNull() {
            val validator = with(DialogInputValidators) { emptyOr(floatValue()) }

            val result = validator.validate(player, "")

            assertThat(result)
                    .isNull()
        }

        @ParameterizedTest
        @ValueSource(strings = ["   ", "abc"])
        fun givenInputTextIsNotEmptyItShouldReturn(nonEmptyInputText: String) {
            val validator = with(DialogInputValidators) { emptyOr(floatValue(errorMessage = "Fail")) }

            val result = validator.validate(player, nonEmptyInputText)

            assertThat(result)
                    .isEqualTo("Fail")
        }

    }

    @Nested
    inner class BlankOrTests {

        @ParameterizedTest
        @ValueSource(strings = ["   ", ""])
        fun givenInputTextIsBlankItShouldReturnNull(blankInputText: String) {
            val validator = blankOr(floatValue())

            val result = validator.validate(player, blankInputText)

            assertThat(result)
                    .isNull()
        }

        @Test
        fun givenInputTextIsNotBlankItShouldReturn() {
            val validator = blankOr(floatValue(errorMessage = "Fail"))

            val result = validator.validate(player, "abc")

            assertThat(result)
                    .isEqualTo("Fail")
        }

    }

    @Nested
    inner class OrTests {

        @ParameterizedTest
        @ValueSource(strings = ["abc", "13.37"])
        fun givenAnyValidationSucceedsItShouldReturnNull(inputText: String) {
            val validator = containedIn("abc", "xyz") or floatValue()

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isNull()
        }

        @ParameterizedTest
        @ValueSource(strings = ["", "   ", "hahaha"])
        fun givenNoValidationSucceedsItShouldReturnError(inputText: String) {
            val validator = containedIn("abc", "xyz", errorMessage = "Fail") or floatValue("Fail")

            val result = validator.validate(player, inputText)

            assertThat(result)
                    .isEqualTo("Fail")
        }

    }

}