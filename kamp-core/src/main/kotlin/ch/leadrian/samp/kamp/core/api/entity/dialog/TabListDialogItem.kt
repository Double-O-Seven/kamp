package ch.leadrian.samp.kamp.core.api.entity.dialog

import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.text.TextKey

interface TabListDialogItem<V : Any> {

    val value: V

    fun getTabbedContent(player: Player): List<String>

    fun onSelect(player: Player, inputText: String)

    interface Builder<V : Any> {

        infix fun value(value: V): Builder<V>

        fun tabbedContent(vararg text: String): Builder<V>

        fun tabbedContent(vararg textKey: TextKey): Builder<V>

        fun tabbedContent(vararg supplier: (Player) -> String): Builder<V>

        fun tabbedContent(vararg supplier: DialogTextSupplier): Builder<V>

        infix fun onSelect(onSelect: TabListDialogItem<V>.(Player, String) -> Unit): Builder<V>

        fun build(): TabListDialogItem<V>
    }

}