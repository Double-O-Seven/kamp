package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.FunctionalDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.ListDialogItem
import ch.leadrian.samp.kamp.core.api.entity.dialog.StringDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.TextKeyDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider

internal class DefaultListDialogItem<V : Any>(
        override val value: V,
        private val contentDialogTextSupplier: DialogTextSupplier,
        private val onSelect: (ListDialogItem<V>.(Player, String) -> Unit)?
) : ListDialogItem<V> {

    override fun getContent(player: Player): String = contentDialogTextSupplier.getText(player)

    override fun onSelect(player: Player, inputText: String) {
        onSelect?.invoke(this, player, inputText)
    }

    internal class Builder<V : Any>(private val textProvider: TextProvider) : ListDialogItem.Builder<V> {

        private lateinit var value: V
        private lateinit var contentDialogTextSupplier: DialogTextSupplier
        private var onSelect: (ListDialogItem<V>.(Player, String) -> Unit)? = null

        override fun value(value: V): Builder<V> {
            this.value = value
            return this
        }

        override fun content(text: String): Builder<V> {
            contentDialogTextSupplier = StringDialogTextSupplier(text)
            return this
        }

        override fun content(textKey: TextKey): Builder<V> {
            contentDialogTextSupplier = TextKeyDialogTextSupplier(textKey, textProvider)
            return this
        }

        override fun content(supplier: (Player) -> String): Builder<V> {
            contentDialogTextSupplier = FunctionalDialogTextSupplier(supplier)
            return this
        }

        override fun content(supplier: DialogTextSupplier): Builder<V> {
            contentDialogTextSupplier = supplier
            return this
        }

        override fun onSelect(onSelect: ListDialogItem<V>.(Player, String) -> Unit): Builder<V> {
            this.onSelect = onSelect
            return this
        }

        override fun build(): ListDialogItem<V> =
                DefaultListDialogItem(
                        value = value,
                        contentDialogTextSupplier = contentDialogTextSupplier,
                        onSelect = onSelect
                )

    }
}