package ch.leadrian.samp.kamp.core.runtime.entity.registry

import ch.leadrian.samp.kamp.core.api.entity.id.DialogId
import ch.leadrian.samp.kamp.core.runtime.entity.dialog.AbstractDialog
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DialogRegistry(private val maxDialogs: Int) {

    @Inject
    constructor() : this(maxDialogs = 32768)

    private val dialogsById: MutableMap<Int, WeakReference<out AbstractDialog>> = hashMapOf()
    private var nextDialogId = 0

    fun <T : AbstractDialog> register(dialogFactory: (DialogId) -> T): T {
        val dialogId = findUnusedDialogId()
        val dialog = dialogFactory(DialogId.valueOf(dialogId))
        dialogsById[dialogId] = WeakReference(dialog)
        return dialog
    }

    operator fun get(dialogId: DialogId): AbstractDialog? = dialogsById[dialogId.value]?.get()

    private fun findUnusedDialogId(): Int {
        var tries = 0
        while (true) {
            nextDialogId = (nextDialogId + 1) % maxDialogs
            if (dialogsById[nextDialogId]?.get() == null) {
                return nextDialogId
            }
            tries++
            if (tries >= maxDialogs) {
                throw IllegalStateException("Could not find free dialog ID")
            }
        }
    }

}