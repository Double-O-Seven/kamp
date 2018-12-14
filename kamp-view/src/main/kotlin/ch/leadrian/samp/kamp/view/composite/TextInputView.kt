package ch.leadrian.samp.kamp.view.composite

import ch.leadrian.samp.kamp.core.TextKeys
import ch.leadrian.samp.kamp.core.api.constants.TextDrawFont
import ch.leadrian.samp.kamp.core.api.data.Color
import ch.leadrian.samp.kamp.core.api.data.Colors
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogInputValidator
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.text.TextFormatter
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.view.ViewContext
import ch.leadrian.samp.kamp.view.base.ClickableView
import ch.leadrian.samp.kamp.view.base.TextTransformer
import ch.leadrian.samp.kamp.view.base.TextTransformers
import ch.leadrian.samp.kamp.view.base.TextView
import ch.leadrian.samp.kamp.view.base.andThen
import ch.leadrian.samp.kamp.view.factory.ViewFactory
import java.util.*

open class TextInputView(
        player: Player,
        viewContext: ViewContext,
        viewFactory: ViewFactory,
        private val dialogService: DialogService,
        private val textFormatter: TextFormatter
) : ClickableView(player, viewContext) {

    companion object {

        private val defaultTextTransformer: TextTransformer = with(TextTransformers) {
            formatAtSign().andThen(removeTilde())
        }

    }

    private lateinit var titleTextView: TextView

    private lateinit var inputTextView: TextView

    private var dialog: Dialog? = null

    var dialogValidationErrorColor: Color = Colors.RED

    var inputTextFont: TextDrawFont
        get() = inputTextView.font
        set(value) {
            inputTextView.font = value
        }

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

    var inputText: String? = null
        private set

    var inputTextTransformer: TextTransformer = defaultTextTransformer

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

    override fun onDraw() {
        if (isInvalidated) {
            dialog = createDialog()
        }
    }

    private fun createDialog(): Dialog {
        return dialogService.createInputDialog {
            validators(validators)
            caption { title }
            leftButton(TextKeys.dialog.button.ok)
            rightButton(TextKeys.dialog.button.cancel)
            onInvalidInput { _, error ->
                message(textFormatter.format(player.locale, "${dialogValidationErrorColor.toEmbeddedString()}{0}", error))
                show(player)
            }
            onSubmit { _, text ->
                message("")
                updateInputText(text)
            }
        }
    }

    override fun onEnable() {
        super.onEnable()
        inputTextView.enable()
    }

    override fun onDisable() {
        super.onDisable()
        inputTextView.disable()
    }

    override fun onDestroy() {
        dialog = null
    }

    private fun updateInputText(inputText: String) {
        this.inputText = inputText
        inputTextView.text = inputText
        onInputChanged()
        draw()
    }

    protected open fun onInputChanged() {}

}