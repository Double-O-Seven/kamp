package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.InputDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.ListDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.MessageBoxDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.TabListDialogBuilder

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