package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.text.TextKey

interface ListDialogItem<V> {

    val value: V

    val content: String

    fun onSelect(player: Player)

    interface Builder<V> {

        fun value(value: V): Builder<V>

        fun content(text: String): Builder<V>

        fun content(textKey: TextKey): Builder<V>

        fun content(supplier: (Player) -> String): Builder<V>

        fun content(supplier: DialogTextSupplier): Builder<V>

        fun onSelect(onSelect: ListDialogItem<V>.(Player) -> Unit): Builder<V>

        fun build(): ListDialogItem<V>
    }

}