package ch.leadrian.samp.kamp.core.runtime.entity.dialog

import ch.leadrian.samp.kamp.core.api.callback.OnDialogResponseListener
import ch.leadrian.samp.kamp.core.api.constants.DialogResponse
import ch.leadrian.samp.kamp.core.api.entity.Player
import ch.leadrian.samp.kamp.core.api.entity.dialog.Dialog
import ch.leadrian.samp.kamp.core.api.entity.id.DialogId

internal abstract class AbstractDialog(override val id: DialogId) : Dialog {

    abstract fun onResponse(player: Player, response: DialogResponse, listItem: Int, inputText: String): OnDialogResponseListener.Result
}
