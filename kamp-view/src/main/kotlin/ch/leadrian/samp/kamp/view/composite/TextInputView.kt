package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.KampCoreTextKeys
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogInputValidator
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.text.MessageFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.BackgroundView
import ch.leadrian.samp.kamp.view.base.ClickableView
import ch.leadrian.samp.kamp.view.base.TextTransformer
import ch.leadrian.samp.kamp.view.base.TextTransformers
import ch.leadrian.samp.kamp.view.base.TextView
import ch.leadrian.samp.kamp.view.base.andThen
import ch.leadrian.samp.kamp.view.base.onClick
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import ch.leadrian.samp.kamp.view.layout.percent
import ch.leadrian.samp.kamp.view.style.Style
import ch.leadrian.samp.kamp.view.style.TextInputStyle
import java.util.Locale

open class TextInputView(
        player: Player,
        viewContext: ViewContext,
        viewFactory: ViewFactory,
        private val dialogService: DialogService,
        private val messageFormatter: MessageFormatter
) : ClickableView(player, viewContext) {

    companion object {

        val DEFAULT_TEXT_TRANSFORMER: TextTransformer = with(TextTransformers) {
            formatAtSign() andThen removeTilde()
        }

    }

    private var titleTextView: TextView

    private var inputBackgroundView: BackgroundView

    private lateinit var inputTextView: TextView

    private val dialog: Dialog by lazy { createDialog() }

    private var validationError: Any? = null

    var inputText: String? = null
        private set

    var isRequired: Boolean = false

    var isPasswordInput: Boolean = false

    var state: State = State.NOT_VALIDATED
        private set

    var invalidInputColor: Color = Colors.RED

    var inputTextFont: TextDrawFont = TextDrawFont.FONT2

    var inputTextOutlineSize: Int
        get() = inputTextView.outlineSize
        set(value) {
            inputTextView.outlineSize = value
        }

    var inputTextShadowSize: Int
        get() = inputTextView.shadowSize
        set(value) {
            inputTextView.shadowSize = value
        }

    var inputTextColor: Color = Colors.DARK_GRAY

    var disabledInputTextColor: Color = Colors.LIGHT_GRAY

    var inputTextBackgroundColor: Color = Colors.DARK_GRAY

    var disabledInputTextBackgroundColor: Color = Colors.LIGHT_GRAY

    var inputBackgroundColor: Color = Colors.WHITE

    var inputTextTransformer: TextTransformer = DEFAULT_TEXT_TRANSFORMER

    var titleFont: TextDrawFont
        get() = titleTextView.font
        set(value) {
            titleTextView.font = value
        }

    var titleOutlineSize: Int
        get() = titleTextView.outlineSize
        set(value) {
            titleTextView.outlineSize = value
        }

    var titleShadowSize: Int
        get() = titleTextView.shadowSize
        set(value) {
            titleTextView.shadowSize = value
        }

    var titleColor: Color = Colors.BLACK

    var titleBackgroundColor: Color
        get() = titleTextView.backgroundColor
        set(value) {
            titleTextView.backgroundColor = value
        }

    var title: String
        get() = titleTextView.text
        set(value) {
            titleTextView.text = value
        }

    fun setTitle(text: String, vararg args: Any) {
        titleTextView.setText(text, *args)
    }

    fun setTitle(textKey: TextKey) {
        titleTextView.setText(textKey)
    }

    fun setTitle(textKey: TextKey, vararg args: Any) {
        titleTextView.setText(textKey, *args)
    }

    fun title(titleSupplier: (Locale) -> String) {
        titleTextView.text(titleSupplier)
    }

    var validators: MutableList<DialogInputValidator> = mutableListOf()

    init {
        with(viewFactory) {
            titleTextView = textView {
                topToTopOf(this@TextInputView)
                height = 40.percent()
                letterHeight = 100.percent()
                color {
                    when (state) {
                        State.INVALID -> invalidInputColor
                        else -> titleColor
                    }
                }
                onClick { this@TextInputView.click() }
            }

            inputBackgroundView = backgroundView {
                bottomToBottomOf(this@TextInputView)
                height = 56.percent()
                color { inputBackgroundColor }
                onClick { this@TextInputView.click() }

                inputTextView = textView {
                    letterHeight = 100.percent()
                    color {
                        when {
                            !this@TextInputView.isEnabled -> disabledInputTextColor
                            state == State.INVALID -> invalidInputColor
                            else -> inputTextColor
                        }
                    }
                    backgroundColor {
                        when {
                            !this@TextInputView.isEnabled -> disabledInputTextBackgroundColor
                            else -> inputTextBackgroundColor
                        }
                    }
                    onClick { this@TextInputView.click() }
                }
            }
        }
        enable()
    }

    fun reset() {
        validationError = null
        state = State.NOT_VALIDATED
        updateInputText(null)
    }

    fun validate() {
        state = if (inputText != null || !isRequired) {
            State.VALID
        } else {
            State.INVALID
        }
    }

    private fun createDialog(): Dialog {
        return dialogService.createInputDialog {
            validators(validators)
            caption { title }
            leftButton(KampCoreTextKeys.dialog.button.ok)
            rightButton(KampCoreTextKeys.dialog.button.cancel)
            isPasswordInput { this@TextInputView.isPasswordInput }
            message { getDialogMessage() }
            onInvalidInput { _, error ->
                validationError = error
                show(player)
            }
            onSubmit { _, text ->
                validationError = null
                state = State.VALID
                updateInputText(text)
            }
        }
    }

    private fun getDialogMessage(): String {
        return validationError?.let {
            messageFormatter.format(
                    player.locale,
                    Colors.DIALOG_TEXT,
                    "{0}{1}",
                    invalidInputColor,
                    it
            )
        } ?: " "
    }

    override fun onClick() {
        dialog.show(player)
    }

    override fun onEnable() {
        super.onEnable()
        titleTextView.enable()
        inputTextView.enable()
        inputBackgroundView.enable()
    }

    override fun onDisable() {
        super.onDisable()
        titleTextView.disable()
        inputTextView.disable()
        inputBackgroundView.disable()
    }

    override fun onDraw() {
        super.onDraw()
        if (isPasswordInput) {
            inputTextView.textTransformer = TextTransformers.passwordize()
            inputTextView.font = TextDrawFont.BANK_GOTHIC
        } else {
            inputTextView.textTransformer = inputTextTransformer
            inputTextView.font = inputTextFont
        }
    }

    override fun applyStyle(style: Style): Boolean {
        super.applyStyle(style)
        if (style is TextInputStyle) {
            invalidInputColor = style.textInputErrorColor
            inputTextFont = style.textInputFont
            inputTextOutlineSize = style.textInputOutlineSize
            inputTextShadowSize = style.textInputShadowSize
            inputTextColor = style.textInputColor
            inputTextBackgroundColor = style.textInputBackgroundColor
            disabledInputTextColor = style.disabledTextInputColor
            disabledInputTextBackgroundColor = style.disabledTextInputBackgroundColor
            inputBackgroundColor = style.textInputFieldColor
            titleFont = style.textInputTitleFont
            titleOutlineSize = style.textInputTitleOutlineSize
            titleShadowSize = style.textInputTitleShadowSize
            titleColor = style.textInputTitleColor
            titleBackgroundColor = style.textInputTitleBackgroundColor
        }
        return false
    }

    private fun updateInputText(inputText: String?) {
        this.inputText = inputText
        inputTextView.text = inputText ?: ""
        onInputChanged()
        draw()
    }

    protected open fun onInputChanged() {}

    enum class State {
        NOT_VALIDATED,
        VALID,
        INVALID
    }

}