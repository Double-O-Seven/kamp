package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.FunctionalDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.StringDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.TabListDialogItem
import ch.leadrian.samp.kamp.core.api.entity.dialog.TextKeyDialogTextSupplier
import ch.leadrian.samp.kamp.core.api.text.TextKey
import ch.leadrian.samp.kamp.core.api.text.TextProvider

internal class DefaultTabListDialogItem<V : Any>(
        override val value: V,
        private val contentDialogTextSuppliers: List<DialogTextSupplier>,
        private val onSelect: (TabListDialogItem<V>.(Player, String) -> Unit)?
) : TabListDialogItem<V> {

    override fun getTabbedContent(player: Player): List<String> = contentDialogTextSuppliers.map { it.getText(player) }

    override fun onSelect(player: Player, inputText: String) {
        onSelect?.invoke(this, player, inputText)
    }

    internal class Builder<V : Any>(private val textProvider: TextProvider) : TabListDialogItem.Builder<V> {

        private lateinit var value: V
        private lateinit var contentDialogTextSuppliers: List<DialogTextSupplier>
        private var onSelect: (TabListDialogItem<V>.(Player, String) -> Unit)? = null

        override fun value(value: V): Builder<V> {
            this.value = value
            return this
        }

        override fun tabbedContent(vararg text: String): Builder<V> {
            contentDialogTextSuppliers = text.map { StringDialogTextSupplier(it) }
            return this
        }

        override fun tabbedContent(vararg textKey: TextKey): Builder<V> {
            contentDialogTextSuppliers = textKey.map { TextKeyDialogTextSupplier(it, textProvider) }
            return this
        }

        override fun tabbedContent(vararg supplier: (Player) -> String): Builder<V> {
            contentDialogTextSuppliers = supplier.map { FunctionalDialogTextSupplier(it) }
            return this
        }

        override fun tabbedContent(vararg supplier: DialogTextSupplier): Builder<V> {
            contentDialogTextSuppliers = supplier.toList()
            return this
        }

        override fun onSelect(onSelect: TabListDialogItem<V>.(Player, String) -> Unit): Builder<V> {
            this.onSelect = onSelect
            return this
        }

        override fun build(): TabListDialogItem<V> =
                DefaultTabListDialogItem(
                        value = value,
                        contentDialogTextSuppliers = contentDialogTextSuppliers,
                        onSelect = onSelect
                )

    }
}