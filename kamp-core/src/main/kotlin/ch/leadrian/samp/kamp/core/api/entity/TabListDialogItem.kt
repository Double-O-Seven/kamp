package ch.leadrian.samp.kamp.core.api.entity

import ch.leadrian.samp.kamp.core.api.text.TextKey

interface TabListDialogItem<V> {

    val value: V

    val tabbedContent: String

    fun onSelect(player: Player)

    interface Builder<V> {

        fun value(value: V): Builder<V>

        fun tabbedContent(vararg text: String): Builder<V>

        fun tabbedContent(vararg textKey: TextKey): Builder<V>

        fun tabbedContent(vararg supplier: (Player) -> String): Builder<V>

        fun tabbedContent(vararg supplier: DialogTextSupplier): Builder<V>

        fun onSelect(onSelect: TabListDialogItem<V>.(Player) -> Unit): Builder<V>

        fun build(): TabListDialogItem<V>
    }

}