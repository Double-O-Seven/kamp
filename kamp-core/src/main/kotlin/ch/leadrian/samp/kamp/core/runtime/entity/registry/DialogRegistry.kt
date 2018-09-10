package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.entity.Dialog
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DialogRegistry(private val maxDialogs: Int) {

    @Inject
    constructor() : this(maxDialogs = 32768)

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