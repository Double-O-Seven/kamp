package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface ListDialogItem<V : Any> {

    val value: V

    fun getContent(player: Player): String

    fun onSelect(player: Player, inputText: String)

    interface Builder<V : Any> {

        infix fun value(value: V): Builder<V>

        infix fun content(text: String): Builder<V>

        infix fun content(textKey: TextKey): Builder<V>

        infix fun content(supplier: (Player) -> String): Builder<V>

        infix fun content(supplier: DialogTextSupplier): Builder<V>

        infix fun onSelect(onSelect: ListDialogItem<V>.(Player, String) -> Unit): Builder<V>

        fun build(): ListDialogItem<V>
    }

}