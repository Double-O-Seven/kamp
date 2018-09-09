package ch.leadrian.samp.kamp.runtime.entity.registry

import ch.leadrian.samp.kamp.api.entity.Dialog
import ch.leadrian.samp.kamp.api.entity.id.DialogId
import java.lang.ref.WeakReference

internal class DialogRegistry(private val maxDialogs: Int = 32768) {

    private val dialogsById: MutableMap<Int, WeakReference<Dialog>> = hashMapOf()

    fun register(dialogFactory: (DialogId) -> Dialog): Dialog {
        cleanUpDialogIds()
        val dialogId = findUnusedDialogId()
        val dialog = dialogFactory(DialogId.valueOf(dialogId))
        dialogsById[dialogId] = WeakReference(dialog)
        return dialog
    }

    operator fun get(dialogId: DialogId): Dialog? = dialogsById[dialogId.value]?.get()

    private fun findUnusedDialogId(): Int = (0 until maxDialogs).first { dialogsById[it]?.get() == null }

    private fun cleanUpDialogIds() {
        dialogsById.values.removeIf { it.get() == null }
    }

}