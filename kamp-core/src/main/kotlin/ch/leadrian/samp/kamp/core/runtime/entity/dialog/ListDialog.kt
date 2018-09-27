package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.constants.DialogStyle
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.dialog.DialogTextSupplier
import ch.leadrian.samp.kamp.core.api.entity.dialog.ListDialogBuilder
import ch.leadrian.samp.kamp.core.api.entity.dialog.ListDialogItem
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.api.text.TextProvider
import ch.leadrian.samp.kamp.core.api.util.loggerFor
import ch.leadrian.samp.kamp.core.runtime.SAMPNativeFunctionExecutor
import ch.leadrian.samp.kamp.core.runtime.entity.registry.DialogRegistry

internal class ListDialog<V : Any>(
        id: DialogId,
        private val captionTextSupplier: DialogTextSupplier,
        private val leftButtonTextSupplier: DialogTextSupplier,
        private val rightButtonTextSupplier: DialogTextSupplier,
        private val onSelectItem: (Dialog.(Player, ListDialogItem<V>, String) -> Unit)?,
        private val onCancel: (Dialog.(Player) -> OnDialogResponseListener.Result)?,
        private val items: List<ListDialogItem<V>>,
        private val nativeFunctionExecutor: SAMPNativeFunctionExecutor
) : AbstractDialog(id) {

    private companion object {

        val log = loggerFor<ListDialog<*>>()

    }

    override fun show(forPlayer: Player) {
        val itemsString = items.joinToString("\n") { it.getContent(forPlayer) }
        log.debug("Dialog {}: Showing for player {}: {}", id.value, forPlayer.id.value, itemsString)
        nativeFunctionExecutor.showPlayerDialog(
                dialogid = id.value,
                playerid = forPlayer.id.value,
                caption = captionTextSupplier.getText(forPlayer),
                button1 = leftButtonTextSupplier.getText(forPlayer),
                button2 = rightButtonTextSupplier.getText(forPlayer),
                style = DialogStyle.LIST.value,
                info = itemsString
        )
    }

    override fun onResponse(player: Player, response: DialogResponse, listItem: Int, inputText: String): OnDialogResponseListener.Result {
        return when (response) {
            DialogResponse.LEFT_BUTTON -> handleLeftButtonClick(player, listItem, inputText)
            DialogResponse.RIGHT_BUTTON -> handleRightButtonClick(player)
        }
    }

    private fun handleLeftButtonClick(player: Player, listItem: Int, inputText: String): OnDialogResponseListener.Result {
        val item = items.getOrNull(listItem)
        return if (item != null) {
            item.onSelect(player, inputText)
            onSelectItem?.invoke(this, player, item, inputText)
            OnDialogResponseListener.Result.Processed
        } else {
            log.warn(
                    "Dialog {}: Invalid dialog item selected by player {}: {}, {} items available",
                    id.value,
                    player.name,
                    listItem,
                    items.size
            )
            onCancel?.invoke(this, player) ?: OnDialogResponseListener.Result.Ignored
        }
    }

    private fun handleRightButtonClick(player: Player) =
            onCancel?.invoke(this, player) ?: OnDialogResponseListener.Result.Ignored

    internal class Builder<V : Any>(
            textProvider: TextProvider,
            private val nativeFunctionExecutor: SAMPNativeFunctionExecutor,
            private val dialogRegistry: DialogRegistry
    ) : AbstractDialogBuilder<ListDialogBuilder<V>>(textProvider), ListDialogBuilder<V> {

        private var items = mutableListOf<ListDialogItem<V>>()

        private var onCancel: (Dialog.(Player) -> OnDialogResponseListener.Result)? = null

        private var onSelectItem: (Dialog.(Player, ListDialogItem<V>, String) -> Unit)? = null

        override fun item(builder: ListDialogItem.Builder<V>.() -> Unit): Builder<V> {
            val item = DefaultListDialogItem.Builder<V>(textProvider).apply(builder).build()
            items.add(item)
            return self()
        }

        override fun item(item: ListDialogItem<V>): Builder<V> {
            items.add(item)
            return self()
        }

        override fun items(vararg item: ListDialogItem<V>): Builder<V> {
            items.addAll(item)
            return self()
        }

        override fun items(items: Collection<ListDialogItem<V>>): Builder<V> {
            this.items.addAll(items)
            return self()
        }

        override fun onCancel(onCancel: Dialog.(Player) -> OnDialogResponseListener.Result): Builder<V> {
            this.onCancel = onCancel
            return self()
        }

        override fun onSelectItem(onSelectItem: Dialog.(Player, ListDialogItem<V>, String) -> Unit): Builder<V> {
            this.onSelectItem = onSelectItem
            return self()
        }

        override fun build(): ListDialog<V> = dialogRegistry.register { dialogId ->
            ListDialog(
                    id = dialogId,
                    captionTextSupplier = captionTextSupplier,
                    leftButtonTextSupplier = leftButtonTextSupplier,
                    rightButtonTextSupplier = rightButtonTextSupplier,
                    items = items,
                    onCancel = onCancel,
                    onSelectItem = onSelectItem,
                    nativeFunctionExecutor = nativeFunctionExecutor
            )
        }

        override fun self(): Builder<V> = this

    }
}