package ch.leadrian.samp.kamp.core.api.service

import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.InputDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.ListDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.MessageBoxDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.TabListDialogBuilder
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.InputDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.ListDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.MessageBoxDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.TabListDialog
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry
import javax.inject.Inject

class DialogService
@Inject
internal constructor(
        private val textProvider: TextProvider,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val dialogRegistry: DialogRegistry
) {

    inline fun createMessageBoxDialog(builderBlock: MessageBoxDialogBuilder.() -> Unit): Dialog =
            newMessageBoxDialogBuilder().apply(builderBlock).build()

    inline fun createInputDialog(builderBlock: InputDialogBuilder.() -> Unit): Dialog =
            newInputDialogBuilder().apply(builderBlock).build()

    inline fun <V : Any> createListDialog(builderBlock: ListDialogBuilder<V>.() -> Unit): Dialog =
            newListDialogBuilder<V>().apply(builderBlock).build()

    inline fun <V : Any> createTabListDialog(builderBlock: TabListDialogBuilder<V>.() -> Unit): Dialog =
            newTabListDialogBuilder<V>().apply(builderBlock).build()

    fun newMessageBoxDialogBuilder(): MessageBoxDialogBuilder =
            MessageBoxDialog.Builder(textProvider, nativeFunctionExecutor, dialogRegistry)

    fun newInputDialogBuilder(): InputDialogBuilder =
            InputDialog.Builder(textProvider, nativeFunctionExecutor, dialogRegistry)

    fun <V : Any> newListDialogBuilder(): ListDialogBuilder<V> =
            ListDialog.Builder(textProvider, nativeFunctionExecutor, dialogRegistry)

    fun <V : Any> newTabListDialogBuilder(): TabListDialogBuilder<V> =
            TabListDialog.Builder(textProvider, nativeFunctionExecutor, dialogRegistry)

}