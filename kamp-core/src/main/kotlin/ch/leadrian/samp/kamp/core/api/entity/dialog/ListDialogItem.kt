package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface ListDialogItem<V : Any> {

    val value: V

    fun getContent(player: Player): String

    fun onSelect(player: Player, inputText: String)

    interface Builder<V : Any> {

        fun value(value: V): Builder<V>

        fun content(text: String): Builder<V>

        fun content(textKey: TextKey): Builder<V>

        fun content(supplier: (Player) -> String): Builder<V>

        fun content(supplier: DialogTextSupplier): Builder<V>

        fun onSelect(onSelect: ListDialogItem<V>.(Player, String) -> Unit): Builder<V>

        fun build(): ListDialogItem<V>
    }

}