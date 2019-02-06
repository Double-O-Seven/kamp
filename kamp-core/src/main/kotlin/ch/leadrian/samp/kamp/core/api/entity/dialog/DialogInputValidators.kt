package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.KampCoreTextKeys
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey
import java.util.regex.Pattern

object DialogInputValidators {

    @JvmOverloads
    @JvmStatic
    fun notBlank(errorMessage: String? = null): DialogInputValidator = NotBlank(errorMessage = errorMessage)

    @JvmStatic
    fun notBlank(errorMessageTextKey: TextKey): DialogInputValidator = NotBlank(errorMessageTextKey = errorMessageTextKey)

    @JvmOverloads
    @JvmStatic
    fun notEmpty(errorMessage: String? = null): DialogInputValidator = NotEmpty(errorMessage = errorMessage)

    @JvmStatic
    fun notEmpty(errorMessageTextKey: TextKey): DialogInputValidator = NotEmpty(errorMessageTextKey = errorMessageTextKey)

    @JvmOverloads
    @JvmStatic
    fun pattern(regex: String, errorMessage: String? = null): DialogInputValidator =
            PatternMatch(errorMessage = errorMessage, regex = regex)

    @JvmStatic
    fun pattern(regex: String, errorMessageTextKey: TextKey): DialogInputValidator =
            PatternMatch(errorMessageTextKey = errorMessageTextKey, regex = regex)

    @JvmOverloads
    @JvmStatic
    fun floatValue(errorMessage: String? = null): DialogInputValidator = FloatValue(errorMessage = errorMessage)

    @JvmStatic
    fun floatValue(errorMessageTextKey: TextKey): DialogInputValidator = FloatValue(errorMessageTextKey = errorMessageTextKey)

    @JvmOverloads
    @JvmStatic
    fun intValue(errorMessage: String? = null): DialogInputValidator = IntValue(errorMessage = errorMessage)

    @JvmStatic
    fun intValue(errorMessageTextKey: TextKey): DialogInputValidator = IntValue(errorMessageTextKey = errorMessageTextKey)

    @JvmOverloads
    @JvmStatic
    fun containedIn(
            vararg allowedValues: String,
            ignoreCase: Boolean = true,
            errorMessage: String? = null
    ): DialogInputValidator =
            ContainedIn(ignoreCase = ignoreCase, errorMessage = errorMessage, allowedValues = *allowedValues)

    @JvmOverloads
    @JvmStatic
    fun containedIn(
            vararg allowedValues: String,
            ignoreCase: Boolean = true,
            errorMessageTextKey: TextKey
    ): DialogInputValidator =
            ContainedIn(
                    ignoreCase = ignoreCase,
                    errorMessageTextKey = errorMessageTextKey,
                    allowedValues = *allowedValues
            )

    @JvmStatic
    fun emptyOr(other: DialogInputValidator): DialogInputValidator = EmptyOr(other)

    @JvmStatic
    fun blankOr(other: DialogInputValidator): DialogInputValidator = BlankOr(other)

}

infix fun DialogInputValidator.or(other: DialogInputValidator): DialogInputValidator = EitherOr(this, other)

private class FloatValue(
        private val errorMessage: String? = null,
        private val errorMessageTextKey: TextKey? = null
) : DialogInputValidator {

    override fun validate(player: Player, inputText: String): Any? {
        return when (inputText.toFloatOrNull()) {
            null -> errorMessageTextKey ?: errorMessage ?: KampCoreTextKeys.dialog.input.validation.error.floatvalue
            else -> null
        }
    }

}

private class IntValue(
        private val errorMessage: String? = null,
        private val errorMessageTextKey: TextKey? = null
) : DialogInputValidator {

    override fun validate(player: Player, inputText: String): Any? {
        return when (inputText.toIntOrNull()) {
            null -> errorMessageTextKey ?: errorMessage ?: KampCoreTextKeys.dialog.input.validation.error.intvalue
            else -> null
        }
    }

}

private class PatternMatch(
        private val errorMessage: String? = null,
        private val errorMessageTextKey: TextKey? = null,
        regex: String
) : DialogInputValidator {

    private val pattern = Pattern.compile(regex)

    override fun validate(player: Player, inputText: String): Any? {
        if (pattern.matcher(inputText).matches()) {
            return null
        }
        return errorMessageTextKey ?: errorMessage ?: KampCoreTextKeys.dialog.input.validation.error.generic
    }

}

private class NotBlank(
        private val errorMessage: String? = null,
        private val errorMessageTextKey: TextKey? = null
) : DialogInputValidator {

    override fun validate(player: Player, inputText: String): Any? {
        if (inputText.isBlank()) {
            return errorMessageTextKey ?: errorMessage ?: KampCoreTextKeys.dialog.input.validation.error.notblank
        }
        return null
    }

}

private class NotEmpty(
        private val errorMessage: String? = null,
        private val errorMessageTextKey: TextKey? = null
) : DialogInputValidator {

    override fun validate(player: Player, inputText: String): Any? {
        if (inputText.isEmpty()) {
            return errorMessageTextKey ?: errorMessage ?: KampCoreTextKeys.dialog.input.validation.error.notempty
        }
        return null
    }

}

private class ContainedIn(
        private val ignoreCase: Boolean,
        private val errorMessage: String? = null,
        private val errorMessageTextKey: TextKey? = null,
        vararg allowedValues: String
) : DialogInputValidator {

    private val allowedValues: Set<String> = when {
        ignoreCase -> allowedValues.asSequence().map { it.toLowerCase() }.toSet()
        else -> allowedValues.toSet()
    }

    override fun validate(player: Player, inputText: String): Any? {
        val contains = when {
            ignoreCase -> allowedValues.contains(inputText.toLowerCase())
            else -> allowedValues.contains(inputText)
        }
        return when {
            contains -> null
            else -> errorMessageTextKey ?: errorMessage ?: KampCoreTextKeys.dialog.input.validation.error.generic
        }
    }

}

private class EmptyOr(private val other: DialogInputValidator) : DialogInputValidator {

    override fun validate(player: Player, inputText: String): Any? {
        if (inputText.isEmpty()) {
            return null
        }
        return other.validate(player, inputText)
    }

}

private class BlankOr(private val other: DialogInputValidator) : DialogInputValidator {

    override fun validate(player: Player, inputText: String): Any? {
        if (inputText.isBlank()) {
            return null
        }
        return other.validate(player, inputText)
    }

}

private class EitherOr(private val first: DialogInputValidator, private val second: DialogInputValidator) :
        DialogInputValidator {

    override fun validate(player: Player, inputText: String): Any? {
        first.validate(player, inputText) ?: return null
        return second.validate(player, inputText)
    }

}
