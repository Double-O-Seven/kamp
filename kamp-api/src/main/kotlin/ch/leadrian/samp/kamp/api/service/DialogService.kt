package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.entity.Dialog
import ch.leadrian.samp.kamp.api.entity.InputDialogBuilder
import ch.leadrian.samp.kamp.api.entity.ListDialogBuilder
import ch.leadrian.samp.kamp.api.entity.MessageBoxDialogBuilder
import ch.leadrian.samp.kamp.api.entity.TabListDialogBuilder

interface DialogService {

    fun createMessageBoxDialog(builderBlock: MessageBoxDialogBuilder.() -> Unit): Dialog

    fun createInputDialog(builderBlock: InputDialogBuilder.() -> Unit): Dialog

    fun <V> createListDialog(builderBlock: ListDialogBuilder<V>.() -> Unit): Dialog

    fun <V> createTabListDialog(builderBlock: TabListDialogBuilder<V>.() -> Unit): Dialog

    fun newMessageBoxDialogBuilder(): MessageBoxDialogBuilder

    fun newInputDialogBuilder(): InputDialogBuilder

    fun <V> newListDialogBuilder(): ListDialogBuilder<V>

    fun <V> newTabListDialogBuilder(): TabListDialogBuilder<V>

}