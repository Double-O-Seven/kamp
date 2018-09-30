package ch.leadrian.samp.kamp.core.runtime.service

import ch.leadrian.samp.kamp.core.api.callback.CallbackListenerManager
import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener.Result
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.InputDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.ListDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.MessageBoxDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.TabListDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.service.DialogService
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.AbstractDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.InputDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.ListDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.MessageBoxDialog
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.TabListDialog
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DialogServiceImpl
@Inject
constructor(
        private val textProvider: TextProvider,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
        private val callbackListenerManager: CallbackListenerManager,
        private val dialogRegistry: DialogRegistry
) : DialogService, OnDialogResponseListener {

    @PostConstruct
    fun initialize() {
        callbackListenerManager.register(this)
    }

    override fun onDialogResponse(
            player: Player,
            dialogId: DialogId,
            response: DialogResponse,
            listItem: Int,
            inputText: String
    ): Result {
        val dialog = dialogRegistry[dialogId]
        if (dialog != null) {
            return propagateDialogResponse(dialog, player, response, listItem, inputText)
        }
        return Result.Ignored
    }

    private fun propagateDialogResponse(
            dialog: AbstractDialog,
            player: Player,
            response: DialogResponse,
            listItem: Int,
            inputText: String
    ): Result {
        val result = dialog.onResponse(player, response, listItem, inputText)
        if (response.isCancelButton && result == Result.Ignored && dialog === player.dialogNavigation.top) {
            player.dialogNavigation.pop()
            return Result.Processed
        }
        return result
    }

    override fun createMessageBoxDialog(builderBlock: MessageBoxDialogBuilder.() -> Unit): Dialog =
            newMessageBoxDialogBuilder().apply(builderBlock).build()

    override fun createInputDialog(builderBlock: InputDialogBuilder.() -> Unit): Dialog =
            newInputDialogBuilder().apply(builderBlock).build()

    override fun <V : Any> createListDialog(builderBlock: ListDialogBuilder<V>.() -> Unit): Dialog =
            newListDialogBuilder<V>().apply(builderBlock).build()

    override fun <V : Any> createTabListDialog(builderBlock: TabListDialogBuilder<V>.() -> Unit): Dialog =
            newTabListDialogBuilder<V>().apply(builderBlock).build()

    override fun newMessageBoxDialogBuilder(): MessageBoxDialogBuilder =
            MessageBoxDialog.Builder(textProvider, nativeFunctionExecutor, dialogRegistry)

    override fun newInputDialogBuilder(): InputDialogBuilder =
            InputDialog.Builder(textProvider, nativeFunctionExecutor, dialogRegistry)

    override fun <V : Any> newListDialogBuilder(): ListDialogBuilder<V> =
            ListDialog.Builder(textProvider, nativeFunctionExecutor, dialogRegistry)

    override fun <V : Any> newTabListDialogBuilder(): TabListDialogBuilder<V> =
            TabListDialog.Builder(textProvider, nativeFunctionExecutor, dialogRegistry)
}